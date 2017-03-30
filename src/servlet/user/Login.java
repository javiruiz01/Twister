package servlet.user;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jruiz on 1/26/17.
 */
public class Login extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text");
        PrintWriter out = resp.getWriter();

        String user = req.getParameter("user");
        String passwd = req.getParameter("passwd");

        JSONObject response = service.user.LoginService.login(user, passwd);
        out.println(response);
    }
}
