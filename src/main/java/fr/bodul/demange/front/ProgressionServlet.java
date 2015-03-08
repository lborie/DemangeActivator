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

package fr.bodul.demange.front;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import fr.bodul.demange.Util;
import fr.bodul.demange.dao.Character;
import fr.bodul.demange.dao.Faction;
import fr.bodul.demange.dao.GenericDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newArrayList;

public class ProgressionServlet extends HttpServlet {

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

        Collections.sort(characterList, new Comparator<Character>() {
            @Override
            public int compare(Character character, Character t1) {
                return t1.getCurrentExperience().compareTo(character.getCurrentExperience());
            }
        });

        List<Character> progressionList = new ArrayList<>(characterList);
        Collections.sort(progressionList, new Comparator<Character>() {
            @Override
            public int compare(Character character, Character t1) {
                return progression(t1).compareTo(progression(character));
            }
        });
        if (progressionList.size() > 9){
            progressionList = progressionList.subList(0, 10);
        }
        List<Integer> experience = Lists.newArrayList(Collections2.transform(progressionList, new Function<Character, Integer>() {
            @Override
            public Integer apply(Character character) {
                return progression(character);
            }
        }));
        req.setAttribute("progressionsNames", progressionList);
        req.setAttribute("progressionsExperiences", experience);

        Set<String> dates = new TreeSet<>();
        for (Character character : characterList) {
            dates.addAll(character.getExperience().keySet());
        }
        for (Character character : characterList) {
            for (String date : dates) {
                if (!character.getExperience().containsKey(date)) {
                    character.getExperience().put(date, null);
                }
            }
        }
        req.setAttribute("characters", characterList);

        req.getRequestDispatcher("/progression.jsp").forward(req, resp);
    }

    private Integer progression(Character character) {
        Map<String, Integer> experienceMap = character.getExperience();
        List<String> dates = new ArrayList<>(experienceMap.keySet());
        Collections.sort(dates);
        Integer lastExperience = experienceMap.get(dates.get(dates.size() - 1));
        Integer firstExperience = (experienceMap.size() > 27)? experienceMap.get(dates.get(dates.size() - 27)): experienceMap.get(dates.get(0));

        return lastExperience - firstExperience;
    }
}
