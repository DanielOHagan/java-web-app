<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="currentUser" scope="session" class="com.danielohagan.webapp.businesslayer.entities.account.User" />
<html>
<head>
    <title>${currentUser.username} | SETTINGS</title>
</head>
    <body>
        <nav>
            <ul>
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
            </ul>
        </nav>
        <h2>Change Password</h2>
        <form name="changePasswordForm" id="changePasswordForm" action="${pageContext.request.contextPath}/account/change-password" method="post">
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

        <c:if test="${requestScope.hasError.equals(\"true\")}">
            <p>${requestScope.errorMessage}</p>
        </c:if>

        <c:if test="${requestScope.hasInfo.equals(\"true\")}">
            <p>${requestScope.infoMessage}</p>
        </c:if>
    </body>
</html>