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
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<Integer> getFriends(int from) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        try {
            Connection conn = BD.Database.getMySQLConnection();
            String query = "SELECT id_to FROM FRIEND WHERE id_from = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, from);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                result.add(rs.getInt("id_to"));
            }
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONArray createListFriendsId (ArrayList<Integer> friends) {
        JSONArray friends_json = new JSONArray();

        for (Object friend: friends) {
            try {
                JSONObject friend_json = new JSONObject().put("id", friend);
                friends_json.put(friend_json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return friends_json;
    }

    public static JSONObject createObjectFriendsId (ArrayList<Integer> friends) {
        JSONObject friends_json = null;
        try {
            friends_json = new JSONObject().put("id", friends);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return friends_json;
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
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
