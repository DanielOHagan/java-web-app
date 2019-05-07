<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>REGISTER</title>
</head>
<body>
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


        <button type="submit" form="registerForm" value="register">LOGIN</button>
    </form>

    <p class="form-error">${errorMessage}</p>
</body>
</html>