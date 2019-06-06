<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>CHAT</title>
        <c:if test="${sessionScope.loggedIn.equals(\"true\")}">
            <script>
                // noinspection JSAnnotator
                const userId = ${sessionScope.get("currentUser").id};
            </script>
        </c:if>
        <script src="${pageContext.request.contextPath}/res/script/websocket.js"></script>
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
                    <li onclick="changeChatSession(client, ${chatSession.id})" style="border: 2px deepskyblue; margin: 0 3px;">
                        <p>${chatSession.name}</p>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <br />
        <div>
            <h2>Chat Session Messages: <span id="currentChatSessionNameWrapper">Chat Session Not Open</span></h2>
            <div id="chatMessageContainer">
                <%--            <c:forEach items="${sessionScope.primaryChatSession.messageList}" var="message">--%>
                <%--                <li>--%>
                <%--                        ${message.body}--%>
                <%--                </li>--%>
                <%--            </c:forEach>--%>
            </div>
        </div>
        <br />
        <br />
        <div>
    <%--        <form name="chatDialogueBoxMessageInputForm" id="chatDialogueBoxMessageInputForm" method="post">--%>
                <label for="chatDialogueBoxMessageInput">
                    Message:
                </label>
                <input id="chatDialogueBoxMessageInput" placeholder="Input Message Here" width="32"/>
                <button onclick="sendMessage()">
                    SEND
                </button>
    <%--        </form>--%>
        </div>
        <!--
            End of POST based system
            TODO:: Change this to WebSocket based when that works
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