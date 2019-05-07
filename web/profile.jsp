<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="profileUser" scope="request" type="com.danielohagan.webapp.businesslayer.entities.account.User"/>
<html>
    <head>
        <title>${profileUser.username} | PROFILE </title>
    </head>
    <body>
        <nav>
            <ul>
                <li>
                    <a href="${pageContext.request.contextPath}/">
                        Home
                    </a>
                </li>
                <c:choose>
                    <c:when test="${sessionScope.loggedIn.equals(\"true\")}">
                        <jsp:useBean id="currentUser" scope="session" type="com.danielohagan.webapp.businesslayer.entities.account.User"/>
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
                    </c:otherwise>
                </c:choose>
            </ul>
        </nav>
    </body>
</html>
