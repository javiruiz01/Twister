package service.user;

import org.json.JSONObject;

/**
 * Created by jruiz on 1/29/17.
 */
public class LoginService {

    public static JSONObject login (String login, String mdp) {
        if ((login == null) || (mdp == null)) {
            return serviceTools.ErrorTools.serviceRefused("wrong arguments", 0);
        }

        if (!serviceTools.UserTools.userExists(login)) {
            return serviceTools.ErrorTools.serviceRefused("User inconnu", 1);
        }

        if (!serviceTools.UserTools.checkPass(login, mdp)) {
            return serviceTools.ErrorTools.serviceRefused("Mot de passe incorrect", 2);
        }

        if (!serviceTools.UserTools.userConnect(login, mdp)) {
            return serviceTools.ErrorTools.serviceRefused("User already connected", 3);
        }

        String key = serviceTools.UserTools.insertConnection(login);

        return serviceTools.ErrorTools.serviceAccepted(key);

    }
}
