package BD;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by jruiz on 1/31/17.
 */
public class Database {

    public static Mongo m = null;
    private DataSource dataSource;
    private static Database database;

    public Database(String jndiname) throws SQLException {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/" + jndiname);
        } catch (NamingException e) {
            throw new SQLException(jndiname + " is missing in JNDI!: " + e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static Connection getMySQLConnection() throws SQLException {
        if (!DBStatic.mysql_pooling) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return (DriverManager.getConnection("jdbc:mysql://" + DBStatic.host + "/" + DBStatic.mysqlDB,
                    DBStatic.username, DBStatic.password));
        } else {
            if (database == null) {
                database = new Database("jdbc/db");
            }
            return (database.getConnection());
        }
    }

    public static DBCollection getMongoCollection(String collection_name) {

        DBCollection collection;
        if (m == null) {
            try {
                m = new Mongo(MongoDBStatic.host, MongoDBStatic.port);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        DB db = m.getDB(MongoDBStatic.database);
//            db.authenticate(MongoDBStatic.user, MongoDBStatic.passwd.toCharArray());
        collection = db.getCollection(collection_name);
        return collection;
    }

    public static DBCollection _getMongoCollection(String collection_name) {
        DBCollection collection;
        if (m == null) {
            try {
                m = new Mongo(MongoDBStatic.host, MongoDBStatic.port);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        DB db = m.getDB(MongoDBStatic.database);
//        System.out.println("[USER] = " + MongoDBStatic.user + " [PASSWD] = " + MongoDBStatic.passwd);
//        Boolean auth = db.authenticate(MongoDBStatic.user, MongoDBStatic.passwd.toCharArray());
//        if (!auth) {
//            System.out.println("WROOONG");
//        }
        collection = db.getCollection(collection_name);
        return collection;
    }
}
