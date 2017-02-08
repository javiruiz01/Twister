package serviceTools;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.util.JSON;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jruiz on 2/7/17.
 */
public class MessageTools {

    public static int id = 0;

    public static JSONObject createMessageJSON(String text, String author, int author_id) {

        // On prend la collection ou on va garder le nouveau message
        DBCollection collection = BD.Database.getMongoCollection();

        BasicDBObject obj = new BasicDBObject();

        obj.put("author_id", author_id);
        obj.put("author", author);
        obj.put("date", new java.util.GregorianCalendar().getTime());
        obj.put("text", text);
        obj.put("comment_id", id);
        id += 1;

        collection.insert(obj);

        JSONObject result = null;
        try {
            result = new JSONObject(JSON.serialize(obj));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
