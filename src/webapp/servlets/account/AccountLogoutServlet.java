package webapp.servlets.account;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AccountLogoutServlet")
public class AccountLogoutServlet extends HttpServlet {

    /**
     *
     */
    public static final String DISPATCHER_PATH_HOME_PAGE = "/index.jsp";

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

    }
}