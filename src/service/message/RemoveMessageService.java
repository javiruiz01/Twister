package service.message;

import org.json.JSONObject;

/**
 * Created by jruiz on 4/9/17.
 */
public class RemoveMessageService {

    public static JSONObject RemoveMessages(String key, Integer id) {

        if ((key == null) || (id == null))
            return serviceTools.ErrorTools.serviceRefused("Wrong arguments", 0);

        int user_id = serviceTools.UserTools.getIdUserFromKey(key);

        if (!serviceTools.UserTools.shouldBeConnected(user_id)) {
            serviceTools.UserTools.deleteConnection(key);
            return serviceTools.ErrorTools.serviceRefused("User should not be connected", 100);
        }

        return serviceTools.MessageTools.removeMessage(id);
    }
}
