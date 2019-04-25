<%--
  Created by IntelliJ IDEA.
  User: Daniel
  Date: 24/04/2019
  Time: 00:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login Page</title>
</head>
    <body>
        <form name="loginForm" id="loginForm" action="${pageContext.request.contextPath}/account/login" method="post">
            <label for="loginFormEmail">Email:</label>
            <input type="text" name="loginFormEmail" id="loginFormEmail" width="32">
            <label for="loginFormPassword">Password:</label>
            <input type="password" name="loginFormPassword" id="loginFormPassword" width="32">

            <button type="submit" form="loginForm" value="login">LOGIN</button>
        </form>

        <p class="form-error">${errorMessage}</p>
    </body>
</html>