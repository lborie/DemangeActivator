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
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newArrayList;

public class DisplayServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        if (Util.login(req, resp)) {
            displayPage(req, resp);
        } else {
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }

    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        if (Util.isLogged(req)) {
            displayPage(req, resp);
        } else {
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    private void displayPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        GenericDao<Character> dao = new GenericDao<>(Character.class);
        List<Character> characterList = dao.getEntities();
        characterList = Lists.newArrayList(Iterables.filter(characterList, new Predicate<Character>() {
            @Override
            public boolean apply(Character character) {
                return (character.isActive() == null || character.isActive()) && character.getName() != null;
            }
        }));
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

        final List<Character> characterListFinal = newArrayList(characterList);
        GenericDao<Faction> daoFactions = new GenericDao<>(Faction.class);
        List<Faction> factions = Lists.newArrayList(Iterables.filter(daoFactions.getEntities(), new Predicate<Faction>() {
            @Override
            public boolean apply(Faction faction) {
                for (Character character : characterListFinal) {
                    try {
                    if (character.getFactionsId() != null && character.getFactionsId().contains(faction.getFactionId())) {
                        return true;
                    }} catch (NullPointerException ex){
                        System.out.print(ex);
                    }
                }
                return false;
            }
        }));
        req.setAttribute("factions", factions);

        req.getRequestDispatcher("/display.jsp").forward(req, resp);
    }
}
