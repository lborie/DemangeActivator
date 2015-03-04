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

package fr.bodul.demange.batch;

import fr.bodul.demange.dao.Character;
import fr.bodul.demange.dao.GenericDao;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CleanServlet extends HttpServlet {


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        GenericDao<Character> daoCharacters = new GenericDao<>(Character.class);
        List<Character> characters = daoCharacters.getEntities();

        for (Character character : characters) {
            if (character.getExperience() != null) {
                character.getExperience().remove("2/18/15 5:14 PM");
            }
        }

        daoCharacters.insertEntities(characters);
    }

}
