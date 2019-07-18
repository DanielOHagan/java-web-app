<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="currentUser" scope="session" class="com.danielohagan.webapp.businesslayer.entities.account.User" />
<html>
<head>
    <meta charset="UTF-8"/>
    <meta name="viewpoint" content="width=device-width, initial-scale=1">
    <title>${currentUser.username} | SETTINGS</title>
</head>
    <body>
        <jsp:include page="views/nav.jsp" />
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
        <br />
        <br />
        <form name="deleteAccountForm" id="deleteAccountForm" action="${pageContext.request.contextPath}/account/delete" method="post">
            <label for="deleteAccountPassword">Password:</label>
            <input type="password" name="deleteAccountPassword" id="deleteAccountPassword" width="32" />
            <br />
            <br />
            <button type="submit" form="deleteAccountForm" value="deleteAccount">DELETE ACCOUNT</button>
        </form>

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
    </body>
</html>