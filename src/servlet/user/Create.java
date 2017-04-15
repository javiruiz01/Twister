package servlet.user;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by jruiz on 1/29/17.
 */
public class Create extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();

        // TODO: Add email and re_passwd

        String login = req.getParameter("login");
        String email = req.getParameter("email");
        String passwd = req.getParameter("passwd");
        String name = req.getParameter("name");
        String lastName = req.getParameter("lastName");

        JSONObject response = service.user.CreateService.Create(name, lastName, email, login, passwd);
        try {
            out.println(response.toString(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
