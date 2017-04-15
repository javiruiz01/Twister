package servlet.message;

import org.json.JSONException;
import org.json.JSONObject;
import service.message.AddCommentService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by jruiz on 3/20/17.
 */
public class AddComment extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/json");
        PrintWriter out = resp.getWriter();

        String key = req.getParameter("key");
        String text = req.getParameter("text");
        Integer message_id = Integer.valueOf(req.getParameter("message_id"));

        JSONObject result = AddCommentService.CreateComment(key, text, message_id);

        try {
            out.println(result.toString(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
