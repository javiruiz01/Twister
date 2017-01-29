package serviceTools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jruiz on 1/29/17.
 */
public class ErrorTools {

    public static JSONObject serviceRefused (String message, int code) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("code", code).put("message",message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static JSONObject serviceAccepted(String key) {

        JSONObject obj = new JSONObject();
        try {
            if (key == null)
                obj.put("code", 200).put("message", "OK").put("key", key);
            else
                obj.put("code", 200).put("message", "OK");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
