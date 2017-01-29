package servlet.user;

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
public class Logout extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Map<String, String[]> param = req.getParameterMap();
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();

        String key = null;
        if (param.containsKey("key")) {
            key = req.getParameter("key");
        }

        JSONObject response = service.user.LogoutService.logout(key);
    }
}
