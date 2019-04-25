<%--
  Created by IntelliJ IDEA.
  User: Daniel
  Date: 24/04/2019
  Time: 19:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome Page</title>
</head>
    <body>
        <nav>
            <ul>
                <li>
                    <a href="${pageContext.request.contextPath}/index.jsp">
                        Home
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/account/logout">
                        Log Out
                    </a>
                </li>
            </ul>
        </nav>
        <h1>Welcome ${username}</h1>
    </body>
</html>