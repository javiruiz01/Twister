package service.friend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jruiz on 2/7/17.
 */
public class ListFriendsService {

    public static JSONArray listFriends(String from) {

        if (from == null)
            return serviceTools.ErrorTools.serviceRefusedList("Wrong arguments", 0);

        if (!serviceTools.UserTools.userExists(from))
            return serviceTools.ErrorTools.serviceRefusedList("Utilisateur " + from + " n'existe pas", 1);

        int from_id = serviceTools.UserTools.getIdUser(from);

        ArrayList friends = serviceTools.FriendTools.getFriends(from_id);
        JSONArray friends_json = new JSONArray();
        for (int i = 0; i < friends.size(); i++) {
            JSONObject friend = serviceTools.FriendTools.createJSON((int) friends.get(i));
            friends_json.put(friend);
        }

        return friends_json;
    }
}
