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
public class Create extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Map<String, String[]> param = req.getParameterMap();
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();

        String login = null;
        String passwd = null;
        String name = null;
        String lastName = null;
        if (param.containsKey("login") && param.containsKey("passwd") &&
                param.containsKey("name") && param.containsKey("lastName")) {
            login = req.getParameter("login");
            passwd = req.getParameter("passwd");
            name = req.getParameter("name");
            lastName = req.getParameter("lastName");
        }

        JSONObject response = service.user.CreateService.Create(login, passwd, name, lastName);
    }
}
