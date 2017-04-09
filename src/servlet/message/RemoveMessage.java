package servlet.message;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by jruiz on 4/9/17.
 */
public class RemoveMessage extends HttpServlet{

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();

        String key = req.getParameter("key");
        Integer id = Integer.valueOf(req.getParameter("id"));

        JSONObject result = service.message.RemoveMessageService.RemoveMessages(key, id);

        out.println(result.toString());
    }
}
