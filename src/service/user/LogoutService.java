package service.user;

import org.json.JSONObject;

import static serviceTools.UserTools.deleteConnection;

/**
 * Created by jruiz on 1/29/17.
 */
public class LogoutService {

    public static JSONObject logout(String key) {

        if (key == null)
            return serviceTools.ErrorTools.serviceRefused("wrong arguments", 0);

        if (!deleteConnection(key))
            return serviceTools.ErrorTools.serviceRefused("User already disconnected", 2);

        return serviceTools.ErrorTools.serviceAccepted(key);
    }
}
