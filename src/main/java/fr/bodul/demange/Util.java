package fr.bodul.demange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

public class Util {

    private final static Properties properties = new Properties();
    private final static Logger logger = LoggerFactory.getLogger("UtilClass");

    public static boolean isLogged(HttpServletRequest req) {
        try {
            properties.load(RefreshServlet.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String estimatedHash = getHash((String) properties.get("login.login") + (String) properties.get("login.password"));

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            String calculhash = null;
            //HttpServletRequest
            for (int i = 0; i < cookies.length; i++) {
                Cookie c = cookies[i];
                if (c.getName().equals("hash")) {
                    calculhash = c.getValue();
                }
            }
            if (calculhash != null && estimatedHash.equals(calculhash)) {
                return true;
            }
        }
        return false;
    }

    public static boolean login(HttpServletRequest req, HttpServletResponse resp) {
        try {
            properties.load(RefreshServlet.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        if (properties.get("login.login").equals(login) && properties.get("login.password").equals(password)) {
            String estimatedHash = getHash((String) properties.get("login.login") + (String) properties.get("login.password"));
            Cookie c = new Cookie("hash", estimatedHash);
            c.setMaxAge(60 * 60);
            resp.addCookie(c);

            return true;
        } else {
            return false;
        }
    }

    public static void logout(HttpServletResponse resp) {
        Cookie c = new Cookie("hash", "");
        resp.addCookie(c);
    }

    private static String getHash(String txt) {
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
