<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>HOME</title>
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
        <main>

        </main>
    </body>
</html>
