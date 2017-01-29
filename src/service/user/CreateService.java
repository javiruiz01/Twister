package service.user;

import org.json.JSONObject;

/**
 * Created by jruiz on 1/29/17.
 */
public class CreateService {

    public static JSONObject Create (String login, String passwd, String name, String lastName) {

        if ((login == null) || (passwd == null) || (name == null) || (lastName == null))
            return serviceTools.ErrorTools.serviceRefused("Wrong parameters", 0);

        if (serviceTools.UserTools.userExists(login))
            return serviceTools.ErrorTools.serviceRefused("User already exists", 1);

        // Il faut faire login directement?

        if (!serviceTools.UserTools.insertUser(login, passwd, name, lastName))
            return serviceTools.ErrorTools.serviceRefused("User could not be inserted", 2);

        return serviceTools.ErrorTools.serviceAccepted(null);
    }
}
