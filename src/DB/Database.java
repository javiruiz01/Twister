package DB;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by jruiz on 1/31/17.
 */
public class Database {

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
        }
//        else {
//            if (database == null) {
//                database = new Database("jdbc/db");
//            }
//            return (database.getConnection());
//        }
        return null;
    }
}
