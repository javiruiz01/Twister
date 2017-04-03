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
            result = new JSONObject().put("message_id", obj.get("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Object getNextSequence(String name) {
        DBCollection collection = BD.Database.getMongoCollection("index");

        BasicDBObject query = new BasicDBObject("_id", name);
        BasicDBObject update = new BasicDBObject("$inc", new BasicDBObject("seq", 1));

        DBObject result = collection.findAndModify(query, update);
        return result.get("seq");
    }

    public static JSONObject createCommentJSON(String text, String author, int author_id, Integer message_id) {
        DBCollection collection = BD.Database.getMongoCollection("messages");

        BasicDBObject obj = new BasicDBObject();

        obj.put("author_id", author_id);
        obj.put("id", getNextSequence("comments_id"));
        obj.put("author", author);
        obj.put("date", new java.util.GregorianCalendar().getTime());
        obj.put("text", text);
        obj.put("message_id", message_id);

        BasicDBObject comment = new BasicDBObject("comments", obj);

        BasicDBObject query = new BasicDBObject("id", message_id);
        BasicDBObject update = new BasicDBObject("$push", comment);

        collection.update(query, update);

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

        while (cursor.hasNext()) {
            try {
                DBObject object = cursor.next();
                JSONObject message = new JSONObject()
                        .put("author_id", object.get("author_id"))
                        .put("author", object.get("author"))
                        .put("date", object.get("date"))
                        .put("id", object.get("id"))
                        .put("text", object.get("text"));
                result.put(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static JSONArray ListFriendMessages(ArrayList friends, Integer id_max, Integer id_min, Integer nb) {
        ArrayList<BasicDBObject> list = new ArrayList<BasicDBObject>();

        for (int i = 0; i < friends.size(); i++) {
            BasicDBObject obj = new BasicDBObject();
            obj.put("author_id", friends.get(i));
            list.add(obj);
        }
        BasicDBObject query = new BasicDBObject();
        query.put("$or", list);
        if (id_max != -1 || id_min != -1) {
            BasicDBObject delimiter = new BasicDBObject();
            if (id_max != -1)
                delimiter.put("$lte", id_max);
            if (id_min != -1)
                delimiter.put("$gte", id_min);
            query.put("id", delimiter);
        }

        DBCollection collection = BD.Database.getMongoCollection("messages");

        DBCursor cursor = collection.find(query);
        cursor.sort(new BasicDBObject("date", -1));
        cursor.limit(nb);

        JSONArray result = new JSONArray();

        while (cursor.hasNext()) {
            DBObject friend = cursor.next();
            try {
                JSONObject friends_mongo = new JSONObject()
                        .put("message_id", friend.get("id"))
                        .put("author_id", friend.get("author_id"))
                        .put("author", friend.get("author"))
                        .put("date", friend.get("date"))
                        .put("text", friend.get("text"));
                if (friend.get("comments") != null) {
                    friends_mongo.put("comments", friend.get("comments"));
                }
                JSONObject userInfo = serviceTools.UserTools.getUserInfo((Integer) friend.get("author_id"));
                friends_mongo.put("name", userInfo.get("name")).put("lastname", userInfo.get("lastName"));
                result.put(friends_mongo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
