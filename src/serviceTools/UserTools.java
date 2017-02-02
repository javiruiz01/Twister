package serviceTools;

import org.mindrot.BCrypt;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.Random;
import java.util.UUID;

/**
 * Created by jruiz on 1/29/17.
 */
public class UserTools {

    private static final int BCRYPT_COST = 10;

    public static boolean userExists(String name) {
        boolean result = false;
        try {
            Connection conn = DB.Database.getMySQLConnection();
            String query = "SELECT * FROM USER WHERE login = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, name);
            ResultSet rs = st.executeQuery();
            result = rs.next();
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean checkPass(String name, String password) {
        boolean result = false;
        try {
            Connection conn = DB.Database.getMySQLConnection();
            // Si on veut chiffrer, faudra descomentar los cambios y cambiar la query
            String query = "SELECT passwd FROM USER WHERE login = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, name);
//            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            result = rs.next();
            if (BCrypt.checkpw(password, rs.getString("passwd")))
                result = true;
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean userConnect(int id) {
        boolean result = false;
        try {
            Connection conn = DB.Database.getMySQLConnection();
            String query = "SELECT * FROM SESSION WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            result = rs.next();
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String insertSession(int id, boolean root) {
        String key = UUID.randomUUID().toString().substring(0, 32);
        try {
            Connection conn = DB.Database.getMySQLConnection();
            String query = "INSERT INTO SESSION (`session_key`, `id`, `root`) VALUES (?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, key);
            st.setInt(2, id);
            st.setBoolean(3, root);
            int res = st.executeUpdate();
            if (res > 0)
                return key;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return key;
    }

    public static int getIdUser(String login) {
        int idUser = -1;
        try {
            Connection conn = DB.Database.getMySQLConnection();
            String query = "SELECT id FROM USER WHERE login = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, login);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                idUser = rs.getInt("id");
            }
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idUser;
    }

    public static boolean deleteConnection(String key) {
        boolean result = false;
        try {
            Connection conn = DB.Database.getMySQLConnection();
            String query = "DELETE FROM SESSION WHERE session_key = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, key);
            int res = st.executeUpdate();
            if (res > 0)
                result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean insertUser(String name, String lastName, String login, String passwd) {

        boolean result = false;
        try {
            Connection conn = DB.Database.getMySQLConnection();

            String hash = BCrypt.hashpw(passwd, BCrypt.gensalt(BCRYPT_COST));

            String query = "INSERT INTO `introWeb`.`USER` (`name`, `lastName`, `login`, `passwd`) VALUES ( ?, ?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, name);
            st.setString(2, lastName);
            st.setString(3, login);
            st.setString(4, hash);
            int res = st.executeUpdate();
            if (res > 0)
                result = true;
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
