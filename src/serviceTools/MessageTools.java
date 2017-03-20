package serviceTools;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jruiz on 2/7/17.
 */
public class MessageTools {

    public static JSONObject createMessageJSON(String text, String author, int author_id) {

        // On prend la collection ou on va garder le nouveau message
        DBCollection collection = BD.Database.getMongoCollection("messages");

        BasicDBObject obj = new BasicDBObject();

        obj.put("author_id", author_id);
        obj.put("id", getNextSequence("messages_id"));
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

    public static Object getNextSequence(String name) {
        DBCollection collection = BD.Database.getMongoCollection("index");

        BasicDBObject query =  new BasicDBObject("_id", name);
        BasicDBObject update = new BasicDBObject("$inc", new BasicDBObject("seq", 1));

        DBObject result = collection.findAndModify(query, update);
        return result.get("seq");
    }

    public static JSONObject createCommentJSON(String text, String author, int author_id, Integer message_id) {
        DBCollection collection = BD.Database.getMongoCollection("comments");

        BasicDBObject obj = new BasicDBObject();

        obj.put("author_id", author_id);
        obj.put("id", getNextSequence("comments_id"));
        obj.put("author", author);
        obj.put("date", new java.util.GregorianCalendar().getTime());
        obj.put("text", text);
        obj.put("message_id", message_id);
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

        DBCollection collection = BD.Database.getMongoCollection("messages");
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

    public static JSONArray ListFriendMessages(ArrayList friends) {
        ArrayList<BasicDBObject> list = new ArrayList<BasicDBObject>();

        for (int i = 0; i < friends.size(); i++) {
            BasicDBObject obj = new BasicDBObject();
            obj.put("author_id", friends.get(i));
            list.add(obj);
        }
        BasicDBObject query = new BasicDBObject();
        query.put("$or", list);

        DBCollection collection = BD.Database.getMongoCollection("messages");

        DBCursor cursor = collection.find(query);
        cursor.sort(new BasicDBObject("date", -1));

        JSONArray result = new JSONArray();

        while (cursor.hasNext()) {
            DBObject friend = cursor.next();
            try {
                JSONObject friends_mongo = new JSONObject()
                        .put("author_id", friend.get("author_id"))
                        .put("author", friend.get("author"))
                        .put("date", friend.get("date"))
                        .put("text", friend.get("text"));
                result.put(friends_mongo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
