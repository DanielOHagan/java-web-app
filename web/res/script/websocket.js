"use strict";

const MESSAGE_PREFIX = "message-";
const SESSION_PREFIX = "chat-session-";
const SESSION_USER_PREFIX  = "session-user-";

class WebSocketClient {

    //Set to -1 to indicate none has been selected
    chatSessionId = -1;

    constructor(protocol, hostName, port, endPoint) {
        this.webSocket = null;
        this.protocol = protocol;
        this.hostName = hostName;
        this.port = port;
        this.endPoint = endPoint;
    }

    getServerUrl() {
        return this.protocol + "://" + this.hostName + ":" + this.port + this.endPoint;
    }

    connect() {
        console.log("Attempting connection");

        try {
            this.webSocket = new WebSocket(
                this.getServerUrl()
            );

            this.webSocket.onopen = function(event) {
                // console.log("onOpen:" + JSON.stringify(event, null, 4));
            };

            this.webSocket.onmessage = function(event) {
                //Print message to console for debugging
                var message = event.data;
                console.log("onMessage:" + JSON.stringify(message, null, 4));

                //Decode message
                decodeMessage(message)
            };

            this.webSocket.onabort = function() {
                if (this.webSocket !== null && client.isOpen()) {
                    this.webSocket.sendCloseMessage();
                }
            };

            this.webSocket.onclose = function(event) {
                console.log("onClose:" + JSON.stringify(event, null, 4));
            };

            this.webSocket.onError = function(event) {
                console.error("onError:" + JSON.stringify(event, null, 4));
            };

        } catch (e) {
            console.error(e);
        }
    }

    getStatus() {
        return this.webSocket.readyState;
    }

    resetChatSession() {
        this.chatSessionId = -1;
    }

    send(message) {
        if (this.webSocket.readyState === WebSocket.OPEN) {
            this.webSocket.send(message);
        } else {
            console.error(
                "webSocket is not open, readyState = " +
                this.webSocket.readyState
            );
        }
    }

    disconnect() {
        if (this.webSocket.readyState === WebSocket.OPEN) {
            this.webSocket.close();
        } else {
            console.error(
                "webSocket is not open. readyState = " +
                this.webSocket.readyState
            );
        }
    }

    openChatSession() {
        //Initialise a chat session connection

        var initAction = {
            action: "init",
            userId: userId,
            chatSessionId: this.chatSessionId
        };

        client.send(JSON.stringify(initAction))
    }

    sendCloseMessage() {
        var closeAction = {
            action: "close",
            userId: userId,
            chatSessionId: this.chatSessionId
        };

        client.send(JSON.stringify(closeAction));
    }

    isOpen() {
        return this.webSocket.readyState === WebSocket.OPEN;
    }

    closePreviousChatSession() {
        var closePreviousAction = {
            action: "closePreviousChat",
            userId: userId,
            chatSessionId: this.chatSessionId
        };

        client.send(JSON.stringify(closePreviousAction));
    }
}

function changeChatSession(client, chatSessionId) {
    if (chatSessionId !== client.chatSessionId) {
        //Close old session
        if (client.chatSessionId !== -1) {
            client.closePreviousChatSession();
        }

        //Clear display
        clearChatSessionDisplay();

        //Change to new ID
        client.chatSessionId = chatSessionId;

        //Load data from new chat session
        client.openChatSession();
    }
}

function displayNewMessage(message) {
    var node = document.getElementById("chatMessageContainer");

    var messageWrapperTag = document.createElement("div");
    messageWrapperTag.setAttribute("id", MESSAGE_PREFIX + message.messageId);

    var messageBodyTag = document.createElement("p");
    messageBodyTag.innerHTML = message.senderUsername + ": " + message.body;
    messageWrapperTag.appendChild(messageBodyTag);

    node.appendChild(messageWrapperTag);
}

function displayOldMessage(message) {
    //Add older message above currently displayed messages

}

function removeMessageFromDisplay(message) {
    var messageNode = document.getElementById(MESSAGE_PREFIX + message.messageId);

    messageNode.parentNode.removeChild(messageNode);
}

function displayUpdatedMessage(message) {
    var messageNode = document.getElementById(MESSAGE_PREFIX + message.id);

    if (messageNode !== null) {
        messageNode.innerHTML = message.body;
    }
}

function sendMessage() {
    var inputNode = document.getElementById("chatDialogueBoxMessageInput");
    var messageBody = inputNode.value;

    if (
        messageBody != null &&
        messageBody.length > 0 &&
        client !== null &&
        client.isOpen()
    ) {
        var addMessageAction = {
            action: "addMessage",
            userId: userId,
            body: messageBody,
            senderId: userId,
            chatSessionId: client.chatSessionId
        };

        client.send(JSON.stringify(addMessageAction));

        //Clear Input box
        inputNode.value = "";
    }
}

function deleteMessage(messageId) {
    if (client !== null && client.isOpen()) {
        var deleteMessageAction = {
            action: "deleteMessage",
            userId: userId,
            chatSessionId: client.chatSessionId,
            messageId: messageId
        };

        client.send(JSON.stringify(deleteMessageAction))
    }
}

function decodeMessage(messageData) {
    var message = JSON.parse(messageData);

    switch (message.action) {
        case "init":
            // updateDisplay(message);
            break;

        case "info":

            break;

        case "error":

            break;

        case "clientDisplayNewMessage":
            displayNewMessage(message.message);
            break;
        case "clientDisplayOlderMessage":
            displayOldMessage(message.message);
            break;
        case "clientRemoveMessage":
            removeMessageFromDisplay(message);
            break;
        case "clientUpdateMessage":
            displayUpdatedMessage(message);
            break;

        case "clientAddChatSession":
            addChatSessionToDisplay(message);
            break;
        case "clientRemoveChatSession":
            removeChatSessionFromDisplay(message);
            break;

        case "clientDisplayUser":
            displayChatUser(message);
            break;
        case "clientRemoveUser":
            removeUserFromDisplay(message);
            break;
        case "updateUserPermission":
            displayUpdatedUserPermission(message);
            break;

        case "clientDisplaySessionName":
            displaySessionName(message);
            break;

        default:
            console.warn("Client did not catch action: " + message.action);
            break;
    }
}

function addUserToSession(userId, targetUserId) {
    if (client !== null && client.isOpen()) {
        var addNewUserMessageAction = {
            action: "addUser",
            userId: userId,
            targetUserId: targetUserId,
            chatSessionId: client.chatSessionId
        };

        client.send(JSON.stringify(addNewUserMessageAction));
    }
}

function removeUserFromSession(userId, targetUserId, chatSessionId) {
    if (client !== null && client.isOpen()) {
        var removeUserAction = {
            action: "removeUser",
            userId: userId,
            targetUserId: targetUserId,
            chatSessionId: chatSessionId
        };

        client.send(JSON.stringify(removeUserAction));
    }
}

function changeUserPermissionLevel(
    userId,
    targetUserId,
    targetPermissionLevel,
    chatSessionId
) {
    if (client !== null && client.isOpen()) {
        var changeUserPermissionAction = {
            action: "changeUserPermission",
            userId: userId,
            targetUserId: targetUserId,
            targetPermissionLevel: targetPermissionLevel,
            chatSessionId: chatSessionId
        };

        client.send(JSON.stringify(changeUserPermissionAction));
    }
}

function createNewChatSession() {
    if (client !== null && client.isOpen()) {
        var createNewChatSessionMessageAction = {
            action: "createNewChatSession",
            userId: userId,
        };

        client.send(JSON.stringify(createNewChatSessionMessageAction));
    }
}

function addNewUser() {
    var targetUserId = prompt("Input new User ID", "-1");

    if (targetUserId !== "-1") {

        var id = parseInt(targetUserId);

        if (!id.isNaN()) {
            addUserToSession(userId, id);
        } else {
            console.error("Failed to parse number from New User ID input");
        }
    }
}

function displayChatUser(message) {
    var memberListNode;
    var listItemNode;
    var isCreator = false;

    switch (message.permissionLevel) {
        case "CREATOR":
            var creatorSpan = document.getElementById("chatSessionCreatorSpan");
            creatorSpan.innerHTML = message.userUsername;
            isCreator = true;
            break;
        case "ADMIN":
            memberListNode = document.getElementById("chatSessionAdminsList");
            break;
        case "MEMBER":
            memberListNode = document.getElementById("chatSessionMembersList");
            break;
        case "OBSERVER":
            memberListNode = document.getElementById("chatSessionObserversList");
            break;
    }

    if (!isCreator) {
        listItemNode = document.createElement("ul");
        listItemNode.setAttribute("id", SESSION_USER_PREFIX + message.userId);
        listItemNode.innerHTML = message.userUsername;
        memberListNode.appendChild(listItemNode);
    }
}

function removeUserFromDisplay(message) {
    if (client !== null && client.isOpen()) {
        if (message.chatSessionId === client.chatSessionId) {
            var listItemNode = document.getElementById(SESSION_USER_PREFIX + message.targetUserId);

            if (listItemNode !== null) {
                listItemNode.parentNode.removeChild(listItemNode);
            }
        }
    }
}

function addChatSessionToDisplay(message) {
    //Add a list item for Chat Session
    var chatSessionListNode = document.getElementById("chatSessionList");

    var chatSessionListItem = document.createElement("li");
    chatSessionListItem.setAttribute(
        "id",
        SESSION_PREFIX + message.chatSessionId
    );
    chatSessionListItem.setAttribute("class", "selectable");
    chatSessionListItem.setAttribute(
        "onclick",
        "changeChatSession(client, " + message.chatSessionId + ")"
    );

    var listItemBodyTag = document.createElement("p");
    listItemBodyTag.innerHTML = message.chatSessionName;

    chatSessionListNode.appendChild(chatSessionListItem);
}

function displaySessionName(message) {
    var name = message.chatSessionName;

    if (name !== null && name.length > 0) {
        document.getElementById("currentChatSessionNameWrapper").innerHTML = name;
    }
}

function removeChatSessionFromDisplay(message) {
    var chatSessionId = message.chatSessionId;

    if (client.chatSessionId === chatSessionId) {
        client.resetChatSession();
        clearChatSessionDisplay();
    }

    var chatSessionListItem = document.getElementById(SESSION_PREFIX + chatSessionId);

    if (chatSessionListItem !== null) {
        chatSessionListItem.parentNode.removeChild(chatSessionListItem);
    }
}

function displayUpdatedUserPermission(message) {

}

function clearChatSessionDisplay() {
    document.getElementById("chatMessageContainer").innerHTML = "";
    document.getElementById("chatSessionAdminList").innerHTML = "";
    document.getElementById("chatSessionMembersList").innerHTML = "";
    document.getElementById("chatSessionObserversList").innerHTML = "";
}

function leaveChatSession(userId, chatSessionId) {
    if (confirm("Are you sure you want to leave the Chat Session?")) {

    }
}

function deleteChatSession(chatSessionId) {
    if (confirm("Are you sure you want to delete the Chat Session?")) {
        if (client !== null && client.isOpen()) {
            var deleteChatSessionAction = {
                action: "deleteChatSession",
                userId: userId,
                chatSessionId: chatSessionId
            };

            client.send(JSON.stringify(deleteChatSessionAction));
        }
    }
}

var client = new WebSocketClient(
    "ws",
    "localhost",
    8080,
    "/Serverless_Web_App_war_exploded/chat"
);

document.onclose = function() {
    if (client !== null && client.isOpen()) {
        client.disconnect();
    }
};


client.connect();