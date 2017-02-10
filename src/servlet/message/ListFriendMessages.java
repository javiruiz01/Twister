package servlet.message;

import org.json.JSONArray;
import org.json.JSONException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by jruiz on 2/8/17.
 */
public class ListFriendMessages extends HttpServlet {

    public void doGet (HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/json");
        PrintWriter out = resp.getWriter();

        String key = req.getParameter("key");

        JSONArray response = service.message.ListFriendMessagesService.ListFriendMessages(key);
        try {
            out.println(response.toString(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
