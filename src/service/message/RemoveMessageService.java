package service.message;

import org.json.JSONObject;

/**
 * Created by jruiz on 4/9/17.
 */
public class RemoveMessageService {

    public static JSONObject RemoveMessages(String key, Integer id) {

        if ((key == null) || (id == null))
            return serviceTools.ErrorTools.serviceRefused("Wrong arguments", 0);

        return serviceTools.MessageTools.removeMessage(id);
    }
}
