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
            <a href="${pageContext.request.contextPath}/account/settings">
                Settings
            </a>
            <br />
            <section>
                <header>
                    <h1>
                        Username: ${currentUser.username}
                    </h1>
                </header>
                <h2>Creation Time: ${currentUser.creationTime}</h2>
                <h2>Email: ${currentUser.email}</h2>
                <h2>User Status: ${currentUser.userStatus}</h2>
            </section>
        </main>
    </body>
</html>
