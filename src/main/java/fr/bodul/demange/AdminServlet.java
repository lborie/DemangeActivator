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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdminServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        if (Util.isLogged(req)) {
            try {
                GenericDao<Character> daoCharacters = new GenericDao<>(Character.class);
                if (req.getParameter("addMatriculeAction") != null && req.getParameter("addMatricule") != null) {
                    Long matriculeToAdd = Long.valueOf(req.getParameter("addMatricule"));
                    Character character = daoCharacters.getEntityById(matriculeToAdd);
                    if (character != null) {
                        character.setActive(true);
                    } else {
                        character = new Character();
                        character.setActive(true);
                        character.setMatricule(matriculeToAdd);
                    }
                    daoCharacters.insertEntity(character);

                } else if (req.getParameter("minusMatriculeAction") != null && req.getParameter("minusMatricule") != null){
                    Long matriculeToDelete = Long.valueOf(req.getParameter("minusMatricule"));
                    Character character = daoCharacters.getEntityById(matriculeToDelete);
                    if (character != null) {
                        character.setActive(false);
                        daoCharacters.insertEntity(character);
                    }
                }
            } catch (NumberFormatException ex) {
                log("bad matricule");
            }

            actionPage(req, resp);
        } else {
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }

    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        if (Util.isLogged(req)) {
            actionPage(req, resp);
        } else {
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    private void actionPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        GenericDao<Character> dao = new GenericDao<>(Character.class);
        List<Character> characterList = dao.getEntities();

        Collections.sort(characterList, new Comparator<Character>() {
            @Override
            public int compare(Character character, Character character2) {
                return character.getMatricule().compareTo(character2.getMatricule());
            }
        });

        req.setAttribute("characters", characterList);

        req.getRequestDispatcher("/admin.jsp").forward(req, resp);
    }
}
