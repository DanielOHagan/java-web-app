<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>REGISTER</title>
    </head>
    <body>
        <nav>
            <ul>
                <c:choose>
                    <c:when test="${sessionScope.loggedIn.equals(\"true\")}">
                        <jsp:useBean id="currentUser" scope="session" type="com.danielohagan.webapp.businesslayer.entities.account.User"/>
                        <li>
                            <a href="${pageContext.request.contextPath}/account/profile?id=${currentUser.id}">
                                Profile
                            </a>
                        </li>
                        <li>
                            <a href="${pageContext.request.contextPath}/account/logout">
                                Log Out
                            </a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li>
                            <a href="${pageContext.request.contextPath}/account/login">
                                Login
                            </a>
                        </li>
                        <li>
                            <a href="${pageContext.request.contextPath}/account/register">
                                Register
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </nav>
        <form name="registerForm" id="registerForm" action="${pageContext.request.contextPath}/account/register" method="post">
            <label for="registerFormUsername">Username:</label>
            <input type="text" name="registerFormUsername" id="registerFormUsername" width="32" />
            <br />
            <br />
            <label for="registerFormEmail">Email:</label>
            <input type="email" name="registerFormEmail" id="registerFormEmail" width="32" />
            <br />
            <label for="registerFormEmailConfirm">Confirm Email:</label>
            <input type="email" name="registerFormEmailConfirm" id="registerFormEmailConfirm" width="32" />
            <br />
            <br />
            <label for="registerFormPassword">Password:</label>
            <input type="password" name="registerFormPassword" id="registerFormPassword" width="32" />
            <br />
            <label for="registerFormPasswordConfirm">Confirm Password:</label>
            <input type="password" name="registerFormPasswordConfirm" id="registerFormPasswordConfirm" width="32" />

            <br />
            <button type="submit" form="registerForm" value="register">REGISTER</button>
        </form>

        <c:if test="${requestScope.hasError.equals(\"true\")}">
            <p class="form-error">${requestScope.errorMessage}</p>
        </c:if>
    </body>
</html>