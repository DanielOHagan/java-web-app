"use strict";

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

            this.webSocket.onabort = function(event) {
                if (this.webSocket !== null && client.isOpen()) {
                    this.webSocket.sendCloseMessage();
                }

                console.log("WebSocket Aborted")
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
}

function changeChatSession(client, chatSessionId) {
    if (chatSessionId !== client.chatSessionId) {
        //Change to new ID
        client.chatSessionId = chatSessionId;

        //Open and load data from new chat session
        client.openChatSession();

        //Update Title

    }
}

function displayNewMessage(message) {
    var node = document.getElementById("chatMessageContainer");

    var messageWrapperTag = document.createElement("div");
    messageWrapperTag.setAttribute("id", "message-" + message.id);

    var messageBodyTag = document.createElement("p");
    messageBodyTag.innerHTML = message.body;
    messageWrapperTag.appendChild(messageBodyTag);

    node.appendChild(messageWrapperTag);
}

function sendMessage() {
    var messageBody = document.getElementById("chatDialogueBoxMessageInput").value;

    if (messageBody != null && messageBody.length > 0) {
        var addMessageAction = {
            action: "addMessage",
            body: messageBody,
            senderId: userId,
            chatSessionId: client.chatSessionId
        };
        if (client.isOpen()) {
            client.send(JSON.stringify(addMessageAction));
        }
    }
}

function deleteMessage() {

}

function decodeMessage(message) {

}

function addUserToSession() {

}

function removeUserFromSession() {

}

function changeUserPermissionLevel() {

}

var client = new WebSocketClient(
    "ws",
    "localhost",
    8080,
    "/Serverless_Web_App_war_exploded/chat"
);

document.onload = function () {
    client.connect();
};

if (client.webSocket === null || !client.webSocket.isOpen()) {
    client.connect();
}