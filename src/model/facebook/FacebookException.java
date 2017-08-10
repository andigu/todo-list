package model.facebook;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Susheel Kona
 */
public class FacebookException extends RuntimeException {
    private static Map<Integer, String> customErrorMessages = new HashMap<Integer, String>(){{
        put(0,      "Exception not specified");
        put(190,    "Your session has expired. Please try logging back in");
        put(100,    "You do not have permission to access that group or user or it doesn't exist. If you are requesting a group, make sure the group is public or closed and you are a member.");
        put(803,    this.get(100));
    }};

    public FacebookException(int errorCode) {
        super(customErrorMessages.get(errorCode));
        System.out.println("rcode: "+ errorCode);
    }
}
