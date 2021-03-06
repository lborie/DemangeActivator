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

import com.google.appengine.repackaged.com.google.common.base.Function;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.common.base.Predicate;
import fr.bodul.demange.dao.Character;
import fr.bodul.demange.dao.Faction;
import fr.bodul.demange.dao.GenericDao;
import fr.bodul.demange.dao.Spy;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.google.appengine.repackaged.com.google.common.collect.Maps.uniqueIndex;
import static com.google.common.collect.Iterables.filter;

public class RefreshServlet extends HttpServlet {

    private final static Properties properties = new Properties();
    private final static String DEMANGE_URL = "http://www.demange-le-jeu.com/demange-v2/perso_events_view.php?id_perso=";
    private final static String DEMANGE_LOGGING = "http://www.demange-le-jeu.com/demange-v2/joueur_connexion.php";
    private final static String DEMANGE_PLAYER_VISU = "http://www.demange-le-jeu.com/demange-v2/jouer.php?index_perso=3";
    private final static String EXPERIENCE = "Expérience :";
    private final static String MATRICULE = "Matricule :";
    private final static String START_CONTAINER = "<span class=\"clair\">";
    private final static String END_CONTAINER = "</span>";
    private final static String START_NAME = "<th colspan=\"2\" align=\"center\">";
    private final static String END_NAME = "</th>";
    private final static String START_FACTION = "<a href=\"faction_presentation.php?id_faction=";
    private final static String START_FACTION_NAME = "target=\"_self\">";
    private final static List<String> MATRICULES = new ArrayList<>();
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private boolean mailSent = false;

    {
        MATRICULES.add("172");
        MATRICULES.add("215");
//        MATRICULES.add("220"); // Bellamy Noiresprit
        MATRICULES.add("230"); // Cyanide
//        MATRICULES.add("242"); // Viper
//        MATRICULES.add("268"); // Arya
//        MATRICULES.add("275"); // Silice
//        MATRICULES.add("276"); // Vindhler
        MATRICULES.add("289"); // Ambre
//        MATRICULES.add("291"); // Teod
        MATRICULES.add("309"); // Muad'Dib
//        MATRICULES.add("320"); // Scarni
        MATRICULES.add("364"); // Amaliana
//        MATRICULES.add("367"); // Orthuc
        MATRICULES.add("379"); //
//        MATRICULES.add("412"); // Marius
        MATRICULES.add("416"); // Tengah Rim
        MATRICULES.add("482"); //
        MATRICULES.add("489"); //
//        MATRICULES.add("527"); //Horace
        MATRICULES.add("609"); // Skalino
        MATRICULES.add("641"); //
        MATRICULES.add("660"); // Cailloux
//        MATRICULES.add("662"); // Lady Hamilton
//        MATRICULES.add("668"); // Lord Winchester
        MATRICULES.add("708"); // Calipso
        MATRICULES.add("768"); //
        MATRICULES.add("877"); // Totor
//        MATRICULES.add("894"); // Lord Bâle
        MATRICULES.add("961"); // Ovah
//        MATRICULES.add("1036"); //Torkil
//        MATRICULES.add("1085"); // Matador
//        MATRICULES.add("1127"); // Marie
//        MATRICULES.add("1195"); //Marijanig
        MATRICULES.add("1212"); // Gontrand
        MATRICULES.add("1225"); // Endeuilleur
        MATRICULES.add("1230"); // Mickaella
        MATRICULES.add("1326"); // Iqbal
//        MATRICULES.add("1427"); // Breth
        MATRICULES.add("1482"); // Kanibal
//        MATRICULES.add("1487"); // Lady Cardigan
        MATRICULES.add("1550"); // Baby Doll
//        MATRICULES.add("1724"); // Haer'Dalis
        MATRICULES.add("1745"); // Mengue
        MATRICULES.add("1853"); // Lilah Vetâla
        MATRICULES.add("1962"); // Attacker
//        MATRICULES.add("2054"); // Yahvé
        MATRICULES.add("2134"); // Hautzouz
        MATRICULES.add("2231"); // Xaphan
        MATRICULES.add("2246"); //
        MATRICULES.add("2613"); // Aurora
        MATRICULES.add("2660"); //
        MATRICULES.add("2667"); //
        MATRICULES.add("3097"); // Ander de Kordor

    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        properties.load(RefreshServlet.class.getResourceAsStream("/config.properties"));
        final HttpClient httpClient = HttpClients.createDefault();
        mailSent = false;

        // Logging
        HttpPost httpPost = new HttpPost(DEMANGE_LOGGING);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("joueur_email", (String) properties.get("credentials.mail")));
        params.add(new BasicNameValuePair("code", (String) properties.get("credentials.code")));
        params.add(new BasicNameValuePair("connexion", "Connexion"));
        params.add(new BasicNameValuePair("page_from", "/demange-v2/joueur_accueil.php"));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httpClient.execute(httpPost);

        extractCharacters(httpClient);
        extractScreenShot(httpClient);
    }

    private void extractCharacters(HttpClient httpClient) throws IOException {
        GenericDao<Character> daoCharacters = new GenericDao<>(Character.class);
        GenericDao<Faction> daoFactions = new GenericDao<>(Faction.class);
        List<Character> characters = daoCharacters.getEntities();

        characters = Lists.newArrayList(filter(characters, new Predicate<Character>() {
            @Override
            public boolean apply(Character character) {
                return character.isActive() == null || character.isActive();
            }
        }));

        Map<Long, Character> charsByMatricules = new HashMap<>(uniqueIndex(characters, new Function<Character, Long>() {
            @Override
            public Long apply(Character character) {
                return character.getMatricule();
            }
        }));


        for (Long matricule : charsByMatricules.keySet()) {
            String matriculeString = String.valueOf(matricule);
            logger.info("Refresh matricule : {}", matriculeString);
            HttpGet httpGet = new HttpGet(DEMANGE_URL + matriculeString);
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                StringWriter writer = new StringWriter();
                IOUtils.copy(response.getEntity().getContent(), writer, "UTF-8");
                response.getEntity().getContent().close();
                writer.close();
                Character current = extractCharacter(writer.toString());
                current.setActive(true);
                List<Faction> factions = extractFactions(writer.toString());
                current.setFactionsId(Lists.transform(factions, new Function<Faction, Long>() {
                    @Override
                    public Long apply(Faction faction) {
                        return faction.getFactionId();
                    }
                }));
                daoFactions.insertEntities(factions);

                Calendar hourPlusFiveMinutes = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
                hourPlusFiveMinutes.add(Calendar.MINUTE, 5);

                if (charsByMatricules.get(current.getMatricule()) == null || charsByMatricules.get(current.getMatricule()).getName() == null) {
                    logger.info("New Character found");
                    current.setActivationDate(hourPlusFiveMinutes.getTime());
                    charsByMatricules.put(current.getMatricule(), current);
                } else if (current.getCurrentExperience() - charsByMatricules.get(current.getMatricule()).getCurrentExperience() == 1
                        && (hourPlusFiveMinutes.get(Calendar.HOUR_OF_DAY) % 2 == 0)
                        && (hourPlusFiveMinutes.get(Calendar.MINUTE) < 15)) {
                    Date activationDate = hourPlusFiveMinutes.getTime();
                    logger.info("New Activation Date for matricule {} : {}", matriculeString, activationDate);
                    current.setActivationDate(activationDate);
                    current.setExperience(charsByMatricules.get(current.getMatricule()).getExperience());
                } else {
                    current.setActivationDate(charsByMatricules.get(current.getMatricule()).getActivationDate());
                    current.setExperience(charsByMatricules.get(current.getMatricule()).getExperience());
                }

                if (current.getExperience() == null) {
                    current.setExperience(new HashMap<String, Integer>());
                }
                current.getExperience().put(new SimpleDateFormat("yy/MM/dd").format(hourPlusFiveMinutes.getTime()), current.getCurrentExperience());

                charsByMatricules.put(current.getMatricule(), current);
            } else {
                logger.warn("Warning, status code : {}", response.getStatusLine().getStatusCode());
            }
        }

        daoCharacters.insertEntities(new ArrayList<>(charsByMatricules.values()));
    }

    private void extractScreenShot(HttpClient httpClient) throws IOException {
        GenericDao<Spy> daoSpys = new GenericDao<>(Spy.class);
        Spy spy = new Spy();
        spy.setSpyId(3L);
        HttpGet httpGet = new HttpGet(DEMANGE_PLAYER_VISU);
        HttpResponse response = httpClient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() == 200) {
            StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, "UTF-8");
            response.getEntity().getContent().close();
            writer.close();
            String htmlElement = writer.toString();
            htmlElement = htmlElement.replaceAll("\\.\\./img/", "http://www.demange-le-jeu.com/img/");
            htmlElement = htmlElement.replaceAll("\"\\./", "http://www.demange-le-jeu.com/demange-v2/");
            Document document = Jsoup.parse(htmlElement);
            Element damier = document.getElementById("damier");
            spy.setContent(damier.html());
        } else {
            logger.warn("Warning, status code : {}", response.getStatusLine().getStatusCode());
        }

        daoSpys.insertEntity(spy);
    }

    Character extractCharacter(String htmlResponse) {
        Character currentCharacter = new Character();
        try {
            String nameWithoutStart = htmlResponse.substring(htmlResponse.indexOf(START_NAME) + START_NAME.length());
            String name = nameWithoutStart.substring(0, nameWithoutStart.indexOf(END_NAME));
            currentCharacter.setName(name);

            String withoutStartMatricule = htmlResponse.substring(htmlResponse.indexOf(MATRICULE));
            String matricule = withoutStartMatricule.substring(withoutStartMatricule.indexOf(START_CONTAINER) + START_CONTAINER.length(), withoutStartMatricule.indexOf(END_CONTAINER));
            currentCharacter.setMatricule(Long.valueOf(matricule));

            String withoutStartExperience = htmlResponse.substring(htmlResponse.indexOf(EXPERIENCE));
            String experience = withoutStartExperience.substring(withoutStartExperience.indexOf(START_CONTAINER) + START_CONTAINER.length(), withoutStartExperience.indexOf(END_CONTAINER));
            currentCharacter.setCurrentExperience(Integer.valueOf(experience));
        } catch (StringIndexOutOfBoundsException exception) {
            // Maybe a subscription problem : sending a mail
            sendMailAlert();
        }

        return currentCharacter;
    }

    List<Faction> extractFactions(String htmlResponse) {
        List<Faction> factions = new ArrayList<>();
        try {
            while (htmlResponse.contains(START_FACTION)) {
                try {
                    Faction currentFaction = new Faction();
                    htmlResponse = htmlResponse.substring(htmlResponse.indexOf(START_FACTION) + START_FACTION.length());
                    currentFaction.setFactionId(Long.valueOf(htmlResponse.substring(0, htmlResponse.indexOf('"'))));
                    htmlResponse = htmlResponse.substring(htmlResponse.indexOf(START_FACTION_NAME) + START_FACTION_NAME.length());
                    currentFaction.setName(htmlResponse.substring(0, htmlResponse.indexOf(" : ")));
                    factions.add(currentFaction);
                } catch (NumberFormatException ex) {
                    logger.info("Faction inexistante");
                }
            }
        } catch (StringIndexOutOfBoundsException exception) {
            // Maybe a subscription problem : sending a mail
            sendMailAlert();
        }

        return factions;
    }

    /**
     * Sending of the mail
     */
    private void sendMailAlert() {
        if (!mailSent) {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);

            String msgBody = "Problème lors du rafraichissement. L'abonnement doit être expiré !";

            try {
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(properties.getProperty("admin.mail"), "Moi"));
                msg.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(properties.getProperty("admin.mail"), "Mr. Moi"));
                msg.setSubject("[Démange] Problème avec le CRON");
                msg.setText(msgBody);
                Transport.send(msg);
            } catch (UnsupportedEncodingException | MessagingException e) {
                logger.warn("Impossible to send the mail");
            }

            mailSent = true;
        }
    }
}
