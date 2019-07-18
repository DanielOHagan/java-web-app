<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <meta charset="UTF-8"/>
        <meta name="viewpoint" content="width=device-width, initial-scale=1">
        <title>LOGIN</title>
    </head>
    <body>
        <jsp:include page="views/nav.jsp" />
        <form name="loginForm" id="loginForm" action="${pageContext.request.contextPath}/account/login" method="post">
            <label for="loginFormEmail">Email:</label>
            <input type="text" name="loginFormEmail" id="loginFormEmail" width="32">
            <label for="loginFormPassword">Password:</label>
            <input type="password" name="loginFormPassword" id="loginFormPassword" width="32">

            <button class="selectable" type="submit" form="loginForm" value="login">LOGIN</button>
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