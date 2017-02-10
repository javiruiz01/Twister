package serviceTools;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jruiz on 2/7/17.
 */
public class MessageTools {

    public static JSONObject createMessageJSON(String text, String author, int author_id) {

        // On prend la collection ou on va garder le nouveau message
        DBCollection collection = BD.Database.getMongoCollection();

        BasicDBObject obj = new BasicDBObject();

        obj.put("author_id", author_id);
        obj.put("author", author);
        obj.put("date", new java.util.GregorianCalendar().getTime());
        obj.put("text", text);

        collection.insert(obj);

        JSONObject result = null;
        try {
            result = new JSONObject(JSON.serialize(obj));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static JSONArray ListUserMessages(int author_id) {
        JSONArray result = new JSONArray();

        DBCollection collection = BD.Database.getMongoCollection();
        BasicDBObject obj = new BasicDBObject();
        obj.put("author_id", author_id);
        DBCursor cursor = collection.find(obj);

        while(cursor.hasNext()) {
            try {
                DBObject object = cursor.next();
                JSONObject message = new JSONObject()
                        .put("author_id", object.get("author_id"))
                        .put("author", object.get("author"))
                        .put("date", object.get("date"))
                        .put("text", object.get("text"));
                result.put(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
