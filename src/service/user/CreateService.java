package service.user;

import org.json.JSONObject;

/**
 * Created by jruiz on 1/29/17.
 */
public class CreateService {

    public static JSONObject Create(String name, String lastName, String login, String passwd) {

        if ((login == null) || (passwd == null) || (name == null) || (lastName == null))
            return serviceTools.ErrorTools.serviceRefused("Wrong parameters", 0);

        if (serviceTools.UserTools.userExists(login))
            return serviceTools.ErrorTools.serviceRefused("User already exists", 1);

        if (!serviceTools.UserTools.insertUser(name, lastName, login, passwd))
            return serviceTools.ErrorTools.serviceRefused("User could not be inserted", 2);

        return serviceTools.ErrorTools.serviceAcceptedEmpty();
    }
}
