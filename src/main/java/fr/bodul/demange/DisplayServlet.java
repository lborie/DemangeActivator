/**
 * Copyright 2012 Google Inc. All Rights Reserved. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.bodul.demange;

import com.google.common.base.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newArrayList;

public class DisplayServlet extends HttpServlet {

    private final static Properties properties = new Properties();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doPost (HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        properties.load(RefreshServlet.class.getResourceAsStream("/config.properties"));
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        if (properties.get("login.login").equals(login) && properties.get("login.password").equals(password)) {
            String estimatedHash = getHash((String) properties.get("login.login") + (String) properties.get("login.password"));
            Cookie c = new Cookie("hash", estimatedHash);
            c.setMaxAge(60 * 60);
            resp.addCookie(c);

            displayPage(req, resp);
        } else {
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }

    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        properties.load(RefreshServlet.class.getResourceAsStream("/config.properties"));
        String estimatedHash = getHash((String) properties.get("login.login") + (String) properties.get("login.password"));

        Cookie[] cookies = req.getCookies();     // request is an instance of type

        if (cookies != null){
            String calculhash = null;
            //HttpServletRequest
            for (int i = 0; i < cookies.length; i++) {
                Cookie c = cookies[i];
                if (c.getName().equals("hash")) {
                    calculhash = c.getValue();
                }
            }
            if(calculhash != null && estimatedHash.equals(calculhash)) {
                displayPage(req, resp);
            } else {
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        } else {
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    private void displayPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        GenericDao<Character> dao = new GenericDao<>(Character.class);
        List<Character> characterList = dao.getEntities();
        String factionId = req.getParameter("factionId");
        if (factionId != null) {
            try {
                final Long currentFactionId = Long.valueOf(factionId);
                req.setAttribute("factionId", currentFactionId);
                characterList = newArrayList(filter(characterList, new Predicate<Character>() {
                    @Override
                    public boolean apply(Character character) {
                        return character.getFactionsId() != null && character.getFactionsId().contains(currentFactionId);
                    }
                }));
            } catch (NumberFormatException ex) {
                System.out.print("bad parameter :" + factionId);
                req.setAttribute("factionId", "ALL");
            }
        } else {
            req.setAttribute("factionId", "ALL");
        }

        Collections.sort(characterList, new Comparator<Character>() {
            @Override
            public int compare(Character character, Character character2) {
                return character.getActivationDate().compareTo(character2.getActivationDate());
            }
        });
        req.setAttribute("characters", characterList);

        GenericDao<Faction> daoFactions = new GenericDao<>(Faction.class);
        List<Faction> factions = daoFactions.getEntities();
        req.setAttribute("factions", factions);

        req.getRequestDispatcher("/display.jsp").forward(req, resp);
    }

    public String getHash(String txt) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(txt.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            logger.error("No such Algo", e);
        }
        return null;
    }
}
