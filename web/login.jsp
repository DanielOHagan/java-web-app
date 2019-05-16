<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>LOGIN</title>
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
        <form name="loginForm" id="loginForm" action="${pageContext.request.contextPath}/account/login" method="post">
            <label for="loginFormEmail">Email:</label>
            <input type="text" name="loginFormEmail" id="loginFormEmail" width="32">
            <label for="loginFormPassword">Password:</label>
            <input type="password" name="loginFormPassword" id="loginFormPassword" width="32">

            <button type="submit" form="loginForm" value="login">LOGIN</button>
        </form>

        <c:if test="${requestScope.hasError.equals(\"true\")}">
            <p class="form-error">${requestScope.errorMessage}</p>
        </c:if>
    </body>
</html>