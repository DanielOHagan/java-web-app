<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ERROR</title>
</head>
    <body>
        <p>An error occurred :(</p>
        <p>Error: ${requestScope.errorMessage}</p>
        <a href="${pageContext.request.contextPath}/">HOME</a>
    </body>
</html>
