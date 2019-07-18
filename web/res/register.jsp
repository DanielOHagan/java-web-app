<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <meta charset="UTF-8"/>
        <meta name="viewpoint" content="width=device-width, initial-scale=1">
        <title>REGISTER</title>
    </head>
    <body>
        <jsp:include page="views/nav.jsp" />
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

            <br />
            <button class="selectable" type="submit" form="registerForm" value="register">REGISTER</button>
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