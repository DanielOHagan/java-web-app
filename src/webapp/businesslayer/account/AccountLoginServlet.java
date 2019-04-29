package webapp.businesslayer.account;

import webapp.applayer.account.User;
import webapp.datalayer.account.DatabaseUser;
import webapp.error.ErrorType;
import webapp.error.IErrorType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AccountLoginServlet extends HttpServlet {

    /**
     * HTML_FORM_USERNAME
     *  The name of the HTML form tag
     *
     * HTML_FORM_PASSWORD
     *  The name of the HTML form tag
     */
    public static final String HTML_FORM_EMAIL = "loginFormEmail";
    public static final String HTML_FORM_PASSWORD = "loginFormPassword";

    /**
     * REQUEST_ATTRIBUTE_ERROR_MESSAGE
     *  The attribute tag used to store the error message
     *
     * REQUEST_ATTRIBUTE_USERNAME
     *  The attribute tag used to store the account's username
     *
     * REQUEST_ATTRIBUTE_EMAIL
     *  The attribute tag used to store the account's email
     */
    public static final String REQUEST_ATTRIBUTE_ERROR_MESSAGE = "errorMessage";
    public static final String REQUEST_ATTRIBUTE_USERNAME = "username";
    public static final String REQUEST_ATTRIBUTE_EMAIL = "email";
    public static final String REQUEST_ATTRIBUTE_USER_ID = "userId";

    /**
     *
     */
    public static final String DISPATCHER_PATH_LOGIN_PAGE = "/login.jsp";
    public static final String DISPATCHER_PATH_WELCOME_PAGE = "/welcome.jsp";

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String inputEmail = request.getParameter(HTML_FORM_EMAIL);
        String inputPassword = request.getParameter(HTML_FORM_PASSWORD);

        /*
        Check the account's input to see if the inputs are valid and if there is a corresponding account
         */
        IErrorType errorType = DatabaseUser.userLoginGetErrorType(
                inputEmail,
                inputPassword
        );

        if (errorType == ErrorType.NO_ERROR) {
            User user = DatabaseUser.getUserFromDatabaseByEmail(inputEmail);

            setRequestAttributes(request, user);

            request.getRequestDispatcher(DISPATCHER_PATH_WELCOME_PAGE)
                    .forward(request, response);
        } else {
            request.setAttribute(
                    REQUEST_ATTRIBUTE_ERROR_MESSAGE,
                    errorType.getErrorMessage()
            );

            request.getRequestDispatcher(DISPATCHER_PATH_LOGIN_PAGE)
                    .forward(request, response);
        }
    }

    private void setRequestAttributes(HttpServletRequest request, User user) {
        request.setAttribute(REQUEST_ATTRIBUTE_USERNAME, user.getUsername());
        request.setAttribute(REQUEST_ATTRIBUTE_EMAIL, user.getEmail());
        request.setAttribute(REQUEST_ATTRIBUTE_USER_ID, user.getId());
    }
}