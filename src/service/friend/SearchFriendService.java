package service.friend;

import org.json.JSONArray;

/**
 * Created by jruiz on 2/8/17.
 */
public class SearchFriendService {

    public static JSONArray SearchFriend(String key, String query) {

        if ((key == null) || (query == null))
            return serviceTools.ErrorTools.serviceRefusedList("Wrong arguments", 0);

        JSONArray friends = serviceTools.FriendTools.searchFriend(query);
        return friends;
    }
}
