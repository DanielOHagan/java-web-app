"use strict";

const MESSAGE_PREFIX = "message-";
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
                console.log(
                    "onMessage:" + JSON.stringify(
                        message,
                    null,
                    4
                    )
                );

                //Decode message
                decodeMessage(message)
            };

            this.webSocket.onabort = function() {
                if (this.webSocket !== null && client.isOpen()) {
                    this.webSocket.sendCloseMessage();
                }
            };

            this.webSocket.onclose = function(event) {
                console.log(
                    "onClose:" + JSON.stringify(
                        event,
                    null,
                    4
                    )
                );
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
        document.getElementById("chatMessageContainer").innerHTML = "";
        document.getElementById("chatSessionAdminList").innerHTML = "";
        document.getElementById("chatSessionMembersList").innerHTML = "";
        document.getElementById("chatSessionObserversList").innerHTML = "";

        //Change to new ID
        client.chatSessionId = chatSessionId;

        //Load data from new chat session
        client.openChatSession();

    }
}

function displayNewMessage(message) {
    var node = document.getElementById("chatMessageContainer");

    var messageWrapperTag = document.createElement("div");
    messageWrapperTag.setAttribute("id", MESSAGE_PREFIX + message.id);

    var messageBodyTag = document.createElement("p");
    messageBodyTag.innerHTML = message.senderUsername + ": " + message.body;
    messageWrapperTag.appendChild(messageBodyTag);

    node.appendChild(messageWrapperTag);
}

function displayOldMessage(message) {

}

function removeMessageFromDisplay(message) {
    var messageNode = document.getElementById(MESSAGE_PREFIX + message.id);

    messageNode.delete();
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

    if (messageBody != null && messageBody.length > 0) {
        var addMessageAction = {
            action: "addMessage",
            userId: userId,
            body: messageBody,
            senderId: userId,
            chatSessionId: client.chatSessionId
        };
        if (client.isOpen()) {
            client.send(JSON.stringify(addMessageAction));
        }

        inputNode.value = "";
    }
}

function deleteMessage(messageId) {
    var deleteMessageAction = {
        action: "deleteMessage",
        userId: userId,
        chatSessionId: client.chatSessionId
    };

    if (client.isOpen()) {
        client.send(JSON.stringify(deleteMessageAction))
    }
}

function decodeMessage(messageData) {
    var message = JSON.parse(messageData);

    switch (message.action) {
        case "init":
            // updateDisplay(message);
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
            displayNewChatSession(message);
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
            console.error("Failed to read action: " + message.action);
            break;
    }
}

function addUserToSession() {

}

function removeUserFromSession() {

}

function changeUserPermissionLevel() {

}

function createNewChatSession() {

}

function addNewUser() {

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
        listItemNode.setAttribute("id", SESSION_USER_PREFIX + message.id);
        listItemNode.innerHTML = message.userUsername;
        memberListNode.appendChild(listItemNode);
    }
}

function removeUserFromDisplay(message) {
    var listItemNode = document.getElementById(SESSION_USER_PREFIX + message.id);

    if (listItemNode !== null) {
        listItemNode.delete();
    }
}

function displayNewChatSession(message) {

}

function displaySessionName(message) {
    var name = message.chatSessionName;

    if (name !== null && name.length > 0) {
        document.getElementById("currentChatSessionNameWrapper").innerHTML = name;
    }
}

function removeChatSessionFromDisplay(message) {

}

function displayUpdatedUserPermission(message) {

}

var client = new WebSocketClient(
    "ws",
    "localhost",
    8080,
    "/Serverless_Web_App_war_exploded/chat"
);

document.onload = function () {
    //client.connect();

    // if (client.webSocket === null || !client.webSocket.isOpen()) {
    //     client.connect();
    //
    //     if (!client.isOpen()) {
    //         alert("Failed to open WebSocket connection, please refresh.")
    //     }
    // }
};

client.connect();