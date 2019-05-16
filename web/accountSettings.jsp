<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="currentUser" scope="session" class="com.danielohagan.webapp.businesslayer.entities.account.User" />
<html>
<head>
    <title>${currentUser.username} | SETTINGS</title>
</head>
    <body>
        <form name="changePasswordForm" id="changePasswordForm" action="${pageContext.request.contextPath}/account/changePassword" method="post">
            <label for="newPassword">New Password:</label>
            <input type="password" name="newPassword" id="newPassword" width="32" />
            <br />
            <label for="newPasswordConfirm">Confirm New Password:</label>
            <input type="password" name="newPasswordConfirm" id="newPasswordConfirm" width="32" />
            <br />
            <label for="oldPassword">Old Password:</label>
            <input type="password" name="oldPassword" id="oldPassword" width="32" />
            <br />
            <br />
            <button type="submit" form="changePasswordForm" value="changePassword">CHANGE PASSWORD</button>
        </form>
    </body>
</html>