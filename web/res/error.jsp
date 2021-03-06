<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta name="viewpoint" content="width=device-width, initial-scale=1">
    <title>ERROR</title>
</head>
    <body>
        <p>An error occurred :(</p>
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
        <a href="${pageContext.request.contextPath}/">HOME</a>
    </body>
</html>
