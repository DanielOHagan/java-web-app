<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <meta charset="UTF-8"/>
        <meta name="viewpoint" content="width=device-width, initial-scale=1">
        <title>CHAT</title>
        <c:if test="${sessionScope.loggedIn.equals(\"true\")}">
            <script>
                // noinspection JSAnnotator
                const userId = ${sessionScope.get("currentUser").id};
            </script>
        </c:if>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/stylesheets/style.css"/>
        <script src="${pageContext.request.contextPath}/res/script/websocket.js"></script>
    </head>
    <body>
    <jsp:include page="views/nav.jsp" />
    <main>
        <h1>Chat Centre</h1>
        <div>
            <h2>My Chat Sessions:</h2>
            <ul id="chatSessionList">
                <c:forEach items="${requestScope.chatSessionList}" var="chatSession">
                    <li id="chatSession-${chatSession.id}" class="selectable" onclick="changeChatSession(client, ${chatSession.id})">
                        <p>${chatSession.name}</p>
                    </li>
                </c:forEach>
            </ul>
            <div>
                <button onclick="createNewChatSession()">CREATE NEW CHAT SESSION</button>
            </div>
        </div>
        <br />
        <div>
            <h2>Chat Session Messages: <span id="currentChatSessionNameWrapper">Chat Session Not Open</span></h2>
            <div id="chatMessageContainer"></div>
        </div>
        <br />
        <br />
        <div>
            <label for="chatDialogueBoxMessageInput">Message: </label>
            <input id="chatDialogueBoxMessageInput" placeholder="Input Message Here" width="32"/>
            <button onclick="sendMessage()">SEND</button>
        </div>
        <c:if test="${requestScope.hasInfo.equals(\"true\")}">
            <h3>INFO</h3>
            <ul id="chatSessionInfoList">
                <c:forEach items="${requestScope.infoList}" var="infoMessage">
                    <li>Info: ${infoMessage}</li>
                </c:forEach>
            </ul>
        </c:if>
        <c:if test="${requestScope.hasError.equals(\"true\")}">
            <h3>ERRORS</h3>
            <ul id="chatSessionErrorList">
                <c:forEach items="${requestScope.errorList}" var="errorMessage">
                    <li>Error: ${errorMessage}</li>
                </c:forEach>
            </ul>
        </c:if>
        <br />
        <br />
        <div>
            <h2>Chat Members</h2>
            <div id="chatSessionUsersWrapper">
                <h3>Creator: <span id="chatSessionCreatorSpan"></span></h3>
                <h3>Admins:</h3>
                <ul id="chatSessionAdminList">

                </ul>
                <h3>Members:</h3>
                <ul id="chatSessionMembersList">

                </ul>
                <h3>Observers:</h3>
                <ul id="chatSessionObserversList">

                </ul>
            </div>
            <br />
            <div>
                <button onclick="addNewUser()">ADD NEW USER</button>
            </div>
        </div>
    </main>
    </body>
</html>