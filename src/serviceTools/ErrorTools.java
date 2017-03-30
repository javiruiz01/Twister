package serviceTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jruiz on 1/29/17.
 */
public class ErrorTools {

    public static JSONObject serviceRefused(String message, int code) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("code", code).put("error", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static JSONObject serviceAcceptedLogin(String key, int id, String login) {
//        JSONArray followers = service.friend.ListFriendsService.listFriends(login);
        JSONObject userInfo = serviceTools.UserTools.getUserInfo(id);
        JSONObject obj = null;
        try {
            obj = new JSONObject()
                    .put("key", key)
                    .put("id", id)
                    .put("login", login)
                    .put("name", userInfo.get("name"))
                    .put("lastname", userInfo.get("lastName"))
                    .put("f", serviceTools.FriendTools.getFriends(id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static JSONObject serviceAcceptedEmpty() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("code", 200).put("message", "OK");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static JSONArray serviceRefusedList(String message, int code) {
        JSONArray result = new JSONArray();
        JSONObject obj = null;
        try {
            obj = new JSONObject().put("error", message).put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result.put(obj);
    }
}
