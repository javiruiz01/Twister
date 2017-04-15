//package test;
//
//import BD.MongoDBStatic;
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientURI;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoCursor;
//import com.mongodb.client.MongoDatabase;
//import org.bson.Document;
//
//
///**
// * Created by jruiz on 2/6/17.
// */
//public class testMongoDB {
//
//    public static void main(String[] args) {
//
//        MongoClientURI connectionString = new MongoClientURI(
//                "mongodb://" + MongoDBStatic.user + ":" + MongoDBStatic.passwd
//                        + "@" + MongoDBStatic.host + "/" + MongoDBStatic.database);
//        MongoClient mongoClient = new MongoClient(connectionString);
//
//        MongoDatabase database = mongoClient.getDatabase("technoweb");
//
//        MongoCollection<Document> collection = database.getCollection("mynewcollection");
//
//        Document doc = new Document("name", "beatriz")
//                .append("lastname", "sanchez")
//                .append("login", "bsanchez")
//                .append("passwd", "123");
//
//        collection.insertOne(doc);
//
////        collection.updateOne({"name":"javier"},{ $set: {"name": "javi"}});
//
//        Document myDoc = collection.find().first();
//        System.out.println(myDoc.toJson().toString());
//
//        MongoCursor<Document> cursor = collection.find().iterator();
//        while (cursor.hasNext()) {
//            System.out.println(cursor.next().toString());
//        }
//    }
//}
