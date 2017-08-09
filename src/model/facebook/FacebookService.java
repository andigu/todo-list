package model.facebook;


import controller.json.StateConverter;
import controller.json.SupportedTypeReference;
import model.User;
import model.group.Group;
import org.apache.http.client.utils.URIBuilder;
import services.EnvironmentVariable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Map;

/**
 * @author Susheel Kona
 */
public class FacebookService {
    public static final String BASE_URL = "https://graph.facebook.com/";
    public static final FacebookService instance = new FacebookService();

    public static FacebookService getInstance() {
        return instance;
    }

    private StateConverter converter = StateConverter.getInstance();
    private FacebookDataHelper helper = FacebookDataHelper.getInstance();

    public FacebookService(){}

    public String getLongLiveToken (String shortToken) throws URISyntaxException, MalformedURLException, IOException {

        // Anonymmous constructor?
        URI uri = new URIBuilder(BASE_URL+"oauth/access_token")
                .addParameter("client_id", EnvironmentVariable.CLIENT_ID)
                .addParameter("client_secret", EnvironmentVariable.CLIENT_SECRET)
                .addParameter("grant_type", "fb_exchange_token")
                .addParameter("fb_exchange_token", shortToken).build();


        URL url = uri.toURL();
        Map<String, String> responseData = converter.fromJson(getResponse(url), SupportedTypeReference.STRING_MAP);
        return responseData.get("access_token");

    }

    public User getUserByAccessToken(String accessToken) throws URISyntaxException, IOException{
        URI uri = new URIBuilder(BASE_URL+"me")
                .addParameter("access_token", accessToken)
                .addParameter("fields", "id,first_name,last_name,email,picture")
                .build();

        //TODO add profile pic support
        Map<String, String> responseData = converter.fromJson(getResponse(uri.toURL()), SupportedTypeReference.STRING_MAP);
        System.out.println(responseData.toString());
        return helper.getUser(getResponse(uri.toURL()));

    }

    public Group getGroupByFacebookId(String id){
        return null;
    }

    private String getResponse(URL url) throws java.io.IOException {
        System.out.println(url.toString());
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
