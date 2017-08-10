package model.facebook;


import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
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
        return helper.getUser(getResponse(uri.toURL()));
    }

    public Group getGroupByFacebookId(String accessToken, String id) throws FacebookException, IOException, URISyntaxException {
        URI uri = new URIBuilder(BASE_URL+id)
//                .addParameter("client_id", EnvironmentVariable.CLIENT_ID)
//                .addParameter("client_secret", EnvironmentVariable.CLIENT_SECRET)
                .addParameter("access_token", accessToken)
                .addParameter("fields", "id,name,privacy,members,picture").build();


        return helper.getGroup(getResponse(uri.toURL()));
    }



    private String getResponse(URL url) throws FacebookException, IOException {
        int responseCode=0;
        HttpURLConnection connection = null;
        BufferedReader in = null;
        try {
            System.out.println(url.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            responseCode = connection.getResponseCode();
            System.out.println(responseCode);
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            return getJson(in);
        } catch (IOException e) {
            in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            throw helper.getException(getJson(in));
        }
    }

    private String getJson(BufferedReader in) throws IOException{
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        in.close();
        return buffer.toString();
    }


}
