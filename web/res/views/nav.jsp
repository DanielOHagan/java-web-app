<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav>
    <ul>
        <li>
            <a href="${pageContext.request.contextPath}/">
                Home
            </a>
        </li>
        <c:choose>
            <c:when test="${sessionScope.loggedIn.equals(\"true\")}">
                <jsp:useBean id="currentUser" scope="session" type="com.danielohagan.webapp.businesslayer.entities.account.User"/>
                <li>
                    <a href="${pageContext.request.contextPath}/account/profile?id=${currentUser.id}">
                        Profile
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/chat/">
                        Chat
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/account/settings">
                        Settings
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/account/logout">
                        Log Out
                    </a>
                </li>
            </c:when>
            <c:otherwise>
                <li>
                    <a href="${pageContext.request.contextPath}/account/login">
                        Login
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/account/register">
                        Register
                    </a>
                </li>
            </c:otherwise>
        </c:choose>
    </ul>
</nav>