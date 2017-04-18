package service.message;

import org.json.JSONObject;

/**
 * Created by jruiz on 2/7/17.
 */
public class AddMessageService {

    public static JSONObject CreateMessage(String key, String text) {

        if ((text == null) || (key == null))
            return serviceTools.ErrorTools.serviceRefused("Wrong arguments", 0);

        // On prend le nom de l'utilisateur et son id, a partir de la cle
        String author = serviceTools.UserTools.getUserFromKey(key);

        int author_id = serviceTools.UserTools.getIdUser(author);

        if (!serviceTools.UserTools.shouldBeConnected(author_id)) {
            serviceTools.UserTools.deleteConnection(key);
            return serviceTools.ErrorTools.serviceRefused("User should not be connected", 100);
        }

        if (author_id < 0)
            return serviceTools.ErrorTools.serviceRefused("Something went wrong with the database", 2);

        JSONObject message = serviceTools.MessageTools.createMessageJSON(text, author, author_id);
        return message;
    }
}
