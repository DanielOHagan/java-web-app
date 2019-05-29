<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>CHAT</title>

    </head>
    <body>
        <jsp:include page="views/nav.jsp" />
        <main>
            <h1>Chat Centre</h1>
            <!--
    
            Commented out for now as initial development will use POST instead of a websocket based system
    
            <div class="chatWrapper">
                <div class="chatSessionListContainer">
    
                </div>
                <div class="chatDialogBoxContainer">
                    <label for="chatDialogueBoxMessageInput">Message: </label>
                    <input id="chatDialogueBoxMessageInput" placeholder="Input Message Here" width="32"/>
                    <button type="button" onclick="sendMessage()">SEND</button>
                </div>
            </div>
            -->
    
            <!--
                TODO:: Remove this form once WebSocket system is working
            -->
            <div>
                <h2>My Chat Sessions:</h2>
                <ul>
                    <c:forEach items="${sessionScope.chatSessionList}" var="chatSession">
                        <li>
                            <a href="${pageContext.request.contextPath}/chat/?chatSessionId=${chatSession.id}">
                                ${chatSession.name}
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <br />
            <div>
                <h2>Chat Session Messages: ${sessionScope.primaryChatSession.name}</h2>
                <ul>
                    <c:forEach items="${sessionScope.primaryChatSession.messageList}" var="message">
                        <li>
                            ${message.body}
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <br />
            <br />
            <div>
                <form name="chatDialogueBoxMessageInputForm" id="chatDialogueBoxMessageInputForm" method="post">
                    <label for="chatDialogueBoxMessageInput">
                        Message:
                    </label>
                    <input id="chatDialogueBoxMessageInput" placeholder="Input Message Here" width="32"/>
                    <button type="submit" form="chatDialogueBoxMessageInputForm">
                        SEND
                    </button>
                </form>
            </div>
            <!--
                End of POST based system
                TODO:: Remove this form once WebSocket system is working
            -->
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

            <br />
            <br />

            <div>
                <h2>Chat Members</h2>
                <c:forEach items="${sessionScope.primaryChatSession.userList}" var="chatSessionUser">
                    <div>
                        Username: ${chatSessionUser.username}
                        <br />
                        Permission Level: ${chatSessionUser.permissionLevel}
                    </div>
                </c:forEach>
            </div>
        </main>
    </body>
</html>
