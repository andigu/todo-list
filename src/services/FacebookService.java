package services;


import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.apache.http.client.utils.URIBuilder;

import javax.ws.rs.core.UriBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 * @author Susheel Kona
 */
public class FacebookService {
    public static final String BASE_URL = "https://graph.facebook.com/oauth/access_token";
    public static final FacebookService instance = new FacebookService();


    public static FacebookService getInstance() {
        return instance;
    }


    public FacebookService(){}

    public String getLongLiveToken (String shortToken) throws URISyntaxException, MalformedURLException, IOException {
        System.out.println("oewrpoierpoiert");
        URI uri = new URIBuilder(BASE_URL)
                .addParameter("client_id", EnvironmentVariable.CLIENT_ID)
                .addParameter("client_secret", EnvironmentVariable.CLIENT_SECRET)
                .addParameter("grant_type", "fb_exchange_token")
                .addParameter("fb_exchange_token", shortToken).build();

        System.out.println("uewruiewr`");


        URL url = uri.toURL();
        System.out.println(url.toString());
        return getResponse(url);

    }

    private String getResponse(URL url) throws java.io.IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        System.out.println(responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer buffer = new StringBuffer();

        String line;
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        in.close();

        return buffer.toString();
    }

}
