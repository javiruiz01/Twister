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
 * Created by jruiz on 3/26/17.
 */
public class GetMessages extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();

        String key = req.getParameter("key");
        String query = req.getParameter("query");
        Integer from = Integer.valueOf(req.getParameter("from"));
        Integer id_max = Integer.valueOf(req.getParameter("id_max"));
        Integer id_min = Integer.valueOf(req.getParameter("id_min"));
        Integer nb = Integer.valueOf(req.getParameter("nb"));

        JSONArray response = service.message.GetMessagesService.GetMessages(key, query, from, id_max, id_min, nb);
        try {
            out.println(response.toString(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
