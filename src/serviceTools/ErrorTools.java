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
            obj.put("code", code).put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static JSONObject serviceAcceptedLogin(String key) {
        JSONObject obj = new JSONObject();
        try {
             obj.put("code", 200).put("message", "OK").put("key", key);
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
            obj = new JSONObject().put("message", message).put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result.put(obj);
    }
}
