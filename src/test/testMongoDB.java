package test;

import BD.MongoDBStatic;
import com.mongodb.*;

import java.net.UnknownHostException;

/**
 * Created by jruiz on 2/7/17.
 */
public class testMongoDB {

    public static void main(String[] args) {
        try {
            Mongo m = new Mongo(MongoDBStatic.host, MongoDBStatic.port);
            DB db = m.getDB(MongoDBStatic.database);
            DBCollection collection = db.getCollection("messages");

//            BasicDBObject obj = new BasicDBObject();
//            obj.put("name", "jruiz");
//
//            collection.insert(obj);

//            serviceTools.MessageTools.createMessageJSON("holita que tal1", "jruiz", 1);
//            serviceTools.MessageTools.createMessageJSON("holita que tal2", "jruiz", 1);
//            serviceTools.MessageTools.createMessageJSON("holita que tal3", "jruiz", 1);
//
//            serviceTools.MessageTools.createMessageJSON("hola", "bsanchez", 3);
//            serviceTools.MessageTools.createMessageJSON("hola2", "bsanchez", 3);

            DBCursor res = collection.find();

            while (res.hasNext()) {
                System.out.println("Result: " + res.next().toString());
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

}
