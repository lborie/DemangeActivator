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

import com.google.appengine.repackaged.com.google.common.base.Function;
import com.google.appengine.repackaged.com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

public class RefreshServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final static Properties properties = new Properties();

    private final static String DEMANGE_URL = "http://www.demange-le-jeu.com/demange-v2/perso_events_view.php?id_perso=";
    private final static String DEMANGE_LOGGING = "http://www.demange-le-jeu.com/demange-v2/joueur_connexion.php";
    private final static String EXPERIENCE = "Expérience :";
    private final static String MATRICULE = "Matricule :";
    private final static String START_CONTAINER = "<span class=\"clair\">";
    private final static String END_CONTAINER = "</span>";
    private final static String START_NAME = "<th colspan=\"2\" align=\"center\">";
    private final static String END_NAME = "</th>";
    private final static List<String> MATRICULES = new ArrayList<>();

    {
//        MATRICULES.add("2022"); // Kanithael
        MATRICULES.add("894"); // Lord Bâle
        MATRICULES.add("668"); // Lord Winchester
        MATRICULES.add("662"); // Lady Hamilton
        MATRICULES.add("1487"); // Lady Cardigan
        MATRICULES.add("291"); // Teod
        MATRICULES.add("275"); // Silice
        MATRICULES.add("320"); // Scarni
        MATRICULES.add("276"); // Vindhler
        MATRICULES.add("242"); // Viper
        MATRICULES.add("367"); // Orthuc
        MATRICULES.add("220"); // Bellamy Noiresprit
        MATRICULES.add("1127"); // Marie
        MATRICULES.add("1724"); // Haer'Dalis
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        properties.load(RefreshServlet.class.getResourceAsStream("/config.properties"));
        final HttpClient httpClient = HttpClients.createDefault();

        GenericDao<Character> dao = new GenericDao<>(Character.class);
        List<Character> characters = dao.getEntities();

        Map<Long, Character> charsByMatricules = new HashMap<>(Maps.uniqueIndex(characters, new Function<Character, Long>() {
            @Override
            public Long apply(Character character) {
                return character.getMatricule();
            }
        }));
        // Logging
        HttpPost httpPost = new HttpPost(DEMANGE_LOGGING);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("joueur_email", (String) properties.get("credentials.mail")));
        params.add(new BasicNameValuePair("code", (String) properties.get("credentials.code")));
        params.add(new BasicNameValuePair("connexion", "Connexion"));
        params.add(new BasicNameValuePair("page_from", "/demange-v2/joueur_accueil.php"));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httpClient.execute(httpPost);

        for (String matricule : MATRICULES) {
            logger.info("Refresh matricule : {}", matricule);
            HttpGet httpGet = new HttpGet(DEMANGE_URL + matricule);
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                StringWriter writer = new StringWriter();
                IOUtils.copy(response.getEntity().getContent(), writer, "UTF-8");
                response.getEntity().getContent().close();
                writer.close();
                Character current = extractCharacter(writer.toString());

                if (charsByMatricules.get(current.getMatricule()) == null) {
                    logger.info("New Character found");
                    current.setActivationDate(new Date());
                    charsByMatricules.put(current.getMatricule(), current);
                } else if (current.getCurrentExperience() - charsByMatricules.get(current.getMatricule()).getCurrentExperience() == 1) {
                    logger.info("New Activation Date for matricule {} : {}", matricule, new Date());
                    current.setActivationDate(new Date());
                } else {
                    current.setActivationDate(charsByMatricules.get(current.getMatricule()).getActivationDate());
                }

                charsByMatricules.put(current.getMatricule(), current);
            } else {
                logger.warn("Warning, status code : {}", response.getStatusLine().getStatusCode());
            }
        }

        dao.insertEntities(new ArrayList<>(charsByMatricules.values()));
    }

    Character extractCharacter(String htmlResponse) {
        Character currentCharacter = new Character();

        String nameWithoutStart = htmlResponse.substring(htmlResponse.indexOf(START_NAME) + START_NAME.length());
        String name = nameWithoutStart.substring(0, nameWithoutStart.indexOf(END_NAME));
        currentCharacter.setName(name);

        String withoutStartMatricule = htmlResponse.substring(htmlResponse.indexOf(MATRICULE));
        String matricule = withoutStartMatricule.substring(withoutStartMatricule.indexOf(START_CONTAINER) + START_CONTAINER.length(), withoutStartMatricule.indexOf(END_CONTAINER));
        currentCharacter.setMatricule(Long.valueOf(matricule));

        String withoutStartExperience = htmlResponse.substring(htmlResponse.indexOf(EXPERIENCE));
        String experience = withoutStartExperience.substring(withoutStartExperience.indexOf(START_CONTAINER) + START_CONTAINER.length(), withoutStartExperience.indexOf(END_CONTAINER));
        currentCharacter.setCurrentExperience(Integer.valueOf(experience));

        return currentCharacter;
    }
}
