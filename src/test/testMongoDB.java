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
            DBCollection collection = db.getCollection("comments");

            BasicDBObject obj = new BasicDBObject();
            obj.put("name", "jruiz");

            collection.insert(obj);

            DBCursor res = collection.find();

            while (res.hasNext()) {
                System.out.println("Result: " + res.next().toString());
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

}
