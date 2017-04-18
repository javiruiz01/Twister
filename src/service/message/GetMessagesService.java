package service.message;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by jruiz on 3/26/17.
 */
public class GetMessagesService {

    public static JSONArray GetMessages(String key, String query,
                                        Integer from, Integer id_max, Integer id_min, Integer nb) {

        if ((key == null) || (from == null) || (id_max == null) || (id_min == null) || (nb == null))
            return serviceTools.ErrorTools.serviceRefusedList("Wrong arguments", 0);

        int user_id = serviceTools.UserTools.getIdUserFromKey(key);

        if (!serviceTools.UserTools.shouldBeConnected(user_id)) {
            serviceTools.UserTools.deleteConnection(key);
            return serviceTools.ErrorTools.serviceRefusedList("User " + user_id + " should not be connected", 100);
        }

        if (!serviceTools.UserTools.userConnect(user_id))
            return serviceTools.ErrorTools.serviceRefusedList("User " + user_id + " is not connected", 1);

        if (from != -1) {
            if (!serviceTools.UserTools.userExistsFromId(from))
                return serviceTools.ErrorTools.serviceRefusedList("User " + from + " does not exist", 2);
        }

        JSONArray result = new JSONArray();

        if (query != null) {
//            result = service.message.SearchMessageService.MapReduce(query);
            result = serviceTools.MessageTools.searchMessage(query);
        } else {
            ArrayList<Integer> friends = new ArrayList<Integer>();
            if (from == -1) {
                friends = serviceTools.FriendTools.getFriends(user_id);
                friends.add(user_id);
            } else {
                friends.add(from);
            }

            result = serviceTools.MessageTools.ListFriendMessages(friends, id_max, id_min, nb);
            return result;
        }

        return result;
    }
}
