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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ActivationServlet extends HttpServlet {

    private final static Properties properties = new Properties();
    private final static String DEMANGE_LOGGING = "http://www.demange-le-jeu.com/demange-v2/joueur_connexion.php";
    private final static String DEMANGE_PLAYER_ACTIVATE = "http://www.demange-le-jeu.com/demange-v2/jouer.php?index_perso=";

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        properties.load(ActivationServlet.class.getResourceAsStream("/config.properties"));
        final HttpClient httpClient = HttpClients.createDefault();

        // Logging
        HttpPost httpPost = new HttpPost(DEMANGE_LOGGING);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("joueur_email", (String) properties.get("credentials.mail")));
        params.add(new BasicNameValuePair("code", (String) properties.get("credentials.code")));
        params.add(new BasicNameValuePair("connexion", "Connexion"));
        params.add(new BasicNameValuePair("page_from", "/demange-v2/joueur_accueil.php"));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httpClient.execute(httpPost);

        for (int i = 0; i < 5; i++) {
            HttpGet httpGet = new HttpGet(DEMANGE_PLAYER_ACTIVATE + i);
            httpClient.execute(httpGet);
        }
    }
}
