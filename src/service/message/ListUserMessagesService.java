package service.message;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by jruiz on 2/8/17.
 */
public class ListUserMessagesService {

    public static JSONArray ListUserMessage(String key) {

        if (key == null)
            return serviceTools.ErrorTools.serviceRefusedList("Wrong arguments", 0);

        int user_id = serviceTools.UserTools.getIdUserFromKey(key);

        JSONArray messages = serviceTools.MessageTools.ListUserMessages(user_id);

        return messages;
    }
}
