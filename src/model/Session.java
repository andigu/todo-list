package model;

/**
 * @author Susheel Kona
 */
public class Session {
    private String loginToken;
    private String facebookTokenKey;

    public Session(String loginToken, String facebookTokenKey) {
        this.loginToken = loginToken;
        this.facebookTokenKey = facebookTokenKey;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public String getFacebookTokenKey() {
        return facebookTokenKey;
    }
}
