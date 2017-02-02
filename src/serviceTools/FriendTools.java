package serviceTools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jruiz on 2/2/17.
 */
public class FriendTools {

    public static boolean isFriend(int from, int to) {
        boolean result = false;
        try {
            Connection conn = DB.Database.getMySQLConnection();
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
            Connection conn = DB.Database.getMySQLConnection();
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

    public static boolean removeFriend (int from, int to) {
        boolean result = false;
        try {
            Connection conn = DB.Database.getMySQLConnection();
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
}
