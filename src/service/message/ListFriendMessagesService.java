package service.message;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by jruiz on 2/8/17.
 */
public class ListFriendMessagesService {

    public static JSONArray ListFriendMessages(String key, String query,
                                               Integer from, Integer id_max, Integer id_min, Integer nb) {

        if ((key == null) || (from == null) || (id_max == null) || (id_min == null) || (nb == null))
            return serviceTools.ErrorTools.serviceRefusedList("Wrong arguments", 0);

        if (query == null)
            System.out.println("Continue normally, else just search according to query");

        int user_id = serviceTools.UserTools.getIdUserFromKey(key);

        ArrayList<Integer> friends = serviceTools.FriendTools.getFriends(user_id);
        friends.add(user_id);

        JSONArray result = serviceTools.MessageTools.ListFriendMessages(friends, id_max, id_min, nb );

        return result;
    }
}
