package service.message;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
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

        ArrayList friends = serviceTools.FriendTools.getFriends(user_id);

        JSONArray friendsMessages = new JSONArray();

        for (int i = 0; i < friends.size(); i++) {
            friendsMessages.put(serviceTools.MessageTools.ListUserMessages((int)friends.get(i)));
        }
        friendsMessages.put(serviceTools.MessageTools.ListUserMessages((int)friends.get(user_id)));

        return friendsMessages;
    }
}
