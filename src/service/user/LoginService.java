package service.user;

import org.json.JSONObject;

/**
 * Created by jruiz on 1/29/17.
 */
public class LoginService {

    public static JSONObject login(String login, String mdp) {
        if ((login == null) || (mdp == null))
            return serviceTools.ErrorTools.serviceRefused("Wrong arguments", 0);

        if (!serviceTools.UserTools.userExists(login))
            return serviceTools.ErrorTools.serviceRefused("The username and password you entered did not match our records. Please double-check and try again.", 1);

        if (!serviceTools.UserTools.checkPass(login, mdp))
            return serviceTools.ErrorTools.serviceRefused("The username and password you entered did not match our records. Please double-check and try again.", 2);

        int id_user = serviceTools.UserTools.getIdUser(login);

        if (id_user < 0)
            return serviceTools.ErrorTools.serviceRefused("Error while connecting to the database. We apologize.", 4);

        if (serviceTools.UserTools.userConnect(id_user))
            return serviceTools.ErrorTools.serviceRefused("User already connected", 3);

        String key = serviceTools.UserTools.insertSession(id_user, false);

        return serviceTools.ErrorTools.serviceAcceptedLogin(key, id_user, login);

    }
}
