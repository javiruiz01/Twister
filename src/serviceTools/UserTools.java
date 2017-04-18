package serviceTools;

import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.BCrypt;

import java.sql.*;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by jruiz on 1/29/17.
 */
public class UserTools {

    private static final int BCRYPT_COST = 10;

    public static boolean userExistsFromId(Integer id) {
        boolean result = false;
        try {
            Connection conn = BD.Database.getMySQLConnection();
            String query = "SELECT * FROM USER WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            result = rs.next();
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Boolean shouldBeConnected(Integer id) {
        Boolean result = false;
        Time date = null;
        try {
            Connection conn = BD.Database.getMySQLConnection();
            String query = "SELECT date FROM SESSION WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                date = rs.getTime("date");
            }
            st.close();
            rs.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Long ellapsed_time = date.getTime() - System.currentTimeMillis();
        if (ellapsed_time < 1.8e+6) {
            result = true;
        }
        return result;
    }

    public static boolean userExists(String name) {
        boolean result = false;
        try {
            Connection conn = BD.Database.getMySQLConnection();
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
            Connection conn = BD.Database.getMySQLConnection();
            String query = "SELECT * FROM USER WHERE login = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, name);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String pass = rs.getString("passwd");
                if (BCrypt.checkpw(password, pass))
                    result = true;
            }
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
            Connection conn = BD.Database.getMySQLConnection();
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
            Connection conn = BD.Database.getMySQLConnection();
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
            Connection conn = BD.Database.getMySQLConnection();
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
            Connection conn = BD.Database.getMySQLConnection();
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

    public static boolean insertUser(String name, String lastName, String login, String email, String passwd) {

        boolean result = false;
        try {
            Connection conn = BD.Database.getMySQLConnection();

            String hash = BCrypt.hashpw(passwd, BCrypt.gensalt(BCRYPT_COST));

            String query = "INSERT INTO USER (`name`, `lastName`, `login`, `email`, `passwd`) VALUES ( ?, ?, ?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, name);
            st.setString(2, lastName);
            st.setString(3, login);
            st.setString(4, email);
            st.setString(5, hash);
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

    public static String getUserFromKey(String key) {
        String result = "notConnected";
        try {
            Connection conn = BD.Database.getMySQLConnection();
            String query = "SELECT login FROM USER, SESSION WHERE SESSION.session_key = ? AND USER.id = SESSION.id";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, key);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                result = rs.getString("login");
            }
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int getIdUserFromKey (String key) {
        int result = -1;
        try {
            Connection conn = BD.Database.getMySQLConnection();
            String query = "SELECT USER.id FROM USER, SESSION WHERE SESSION.session_key = ? AND USER.id = SESSION.id";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, key);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                result = rs.getInt("id");
            }
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getUserInfo (int id_user) {
        JSONObject result = new JSONObject();
        try {
            Connection conn = BD.Database.getMySQLConnection();
            String query = "SELECT name, lastName, login FROM USER WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, id_user);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                try {
                    result.put("name", rs.getString("name"))
                            .put("lastName", rs.getString("lastName"))
                            .put("login", rs.getString("login"))
                            .put("id", id_user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
