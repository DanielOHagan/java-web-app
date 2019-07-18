<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="profileUser" scope="request" type="com.danielohagan.webapp.businesslayer.entities.account.User"/>
<html>
    <head>
        <meta charset="UTF-8"/>
        <meta name="viewpoint" content="width=device-width, initial-scale=1">
        <title>${profileUser.username} | PROFILE </title>
    </head>
    <body>
        <jsp:include page="views/nav.jsp" />
        <main>
            <section>
                <header>
                    <h1>
                        Username: ${profileUser.username}
                    </h1>
                </header>
                <h2>ID: ${profileUser.id}</h2>
                <h2>Creation Time: ${profileUser.creationTime}</h2>
                <h2>Email: ${profileUser.email}</h2>
                <h2>User Status: ${profileUser.userStatus}</h2>
            </section>
            <c:if test="${requestScope.hasInfo.equals(\"true\")}">
                <h3>INFO</h3>
                <ul>
                    <c:forEach items="${requestScope.infoList}" var="infoMessage">
                        <li>
                            Info: ${infoMessage}
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
            <c:if test="${requestScope.hasError.equals(\"true\")}">
                <h3>ERRORS</h3>
                <ul>
                    <c:forEach items="${requestScope.errorList}" var="errorMessage">
                        <li>
                            Error: ${errorMessage}
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </main>
    </body>
</html>
