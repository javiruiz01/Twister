package serviceTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by jruiz on 2/2/17.
 */
public class FriendTools {

    public static boolean isFriend(int from, int to) {
        boolean result = false;
        try {
            Connection conn = BD.Database.getMySQLConnection();
            String query = "SELECT * FROM FRIEND WHERE id_from = ? AND id_to = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, from);
            st.setInt(2, to);
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

    public static boolean insertFriend(int from, int to) {
        boolean result = false;
        try {
            Connection conn = BD.Database.getMySQLConnection();
            String query = "INSERT INTO FRIEND (`id_from`, `id_to`) VALUES (?, ?)";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, from);
            st.setInt(2, to);
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

    public static boolean removeFriend(int from, int to) {
        boolean result = false;
        try {
            Connection conn = BD.Database.getMySQLConnection();
            String query = "DELETE FROM FRIEND WHERE id_from = ? AND id_to = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, from);
            st.setInt(2, to);
            if (st.executeUpdate() > 0)
                result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList getFriends(int from) {
        ArrayList result = new ArrayList();
        try {
            Connection conn = BD.Database.getMySQLConnection();
            String query = "SELECT id_to FROM FRIEND WHERE id_from = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, from);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                result.add(rs.getInt("id_to"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject createJSON(int user) {
        JSONObject result = new JSONObject();
        try {
            Connection conn = BD.Database.getMySQLConnection();
            String query = "SELECT name, lastName, login FROM USER WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, user);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                result.put("name", rs.getString("name"))
                        .put("lastName", rs.getString("lastName"))
                        .put("login", rs.getString("login"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONArray searchFriend(String login) {
        JSONArray result = new JSONArray();
        try {
            Connection conn = BD.Database.getMySQLConnection();
            String query = "SELECT id FROM USER WHERE name LIKE ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, login);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                JSONObject userInfo = serviceTools.UserTools.getUserInfo(rs.getInt("id"));
                result.put(userInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
