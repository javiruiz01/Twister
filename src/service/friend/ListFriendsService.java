package service.friend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jruiz on 2/7/17.
 */
public class ListFriendsService {

    public static JSONObject listFriends(String from) {

        // For JSONArray, uncomment
//        if (from == null)
//            return serviceTools.ErrorTools.serviceRefusedList("Wrong arguments", 0);
//
//        if (!serviceTools.UserTools.userExists(from))
//            return serviceTools.ErrorTools.serviceRefusedList("Utilisateur " + from + " n'existe pas", 1);
//
//        int from_id = serviceTools.UserTools.getIdUser(from);
//
//        ArrayList friends = serviceTools.FriendTools.getFriends(from_id);
//        JSONArray friends_json = serviceTools.FriendTools.createListFriendsId(friends);
//        return friends_json;

        if (from == null)
            return serviceTools.ErrorTools.serviceRefused("Wrong arguments", 0);

        if (!serviceTools.UserTools.userExists(from))
            return serviceTools.ErrorTools.serviceRefused("Utilisateur " + from + " n'existe pas", 1);

        int from_id = serviceTools.UserTools.getIdUser(from);

        ArrayList friends = serviceTools.FriendTools.getFriends(from_id);
        return serviceTools.FriendTools.createObjectFriendsId(friends);
    }
}
