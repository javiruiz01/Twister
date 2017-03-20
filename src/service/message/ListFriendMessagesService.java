package service.message;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by jruiz on 2/8/17.
 */
public class ListFriendMessagesService {

    public static JSONArray ListFriendMessages(String key) {

        if (key == null)
            return serviceTools.ErrorTools.serviceRefusedList("Wrong arguments", 0);

        int user_id = serviceTools.UserTools.getIdUserFromKey(key);

        ArrayList<Integer> friends = serviceTools.FriendTools.getFriends(user_id);
        friends.add(user_id);

        JSONArray result = serviceTools.MessageTools.ListFriendMessages(friends);

        return result;
    }
}
