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

        if (from == null)
            return serviceTools.ErrorTools.serviceRefused("Wrong arguments", 0);

        if (!serviceTools.UserTools.userExists(from))
            return serviceTools.ErrorTools.serviceRefused("User " + from + " doesn't exist", 1);

        int from_id = serviceTools.UserTools.getIdUser(from);

        ArrayList friends = serviceTools.FriendTools.getFriends(from_id);
        return serviceTools.FriendTools.createObjectFriendsId(friends);
    }
}
