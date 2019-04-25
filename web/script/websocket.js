"use strict";

class WebSocketClient {

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
            this.webSocket = new WebSocket(this.getServerUrl());

            this.webSocket.onopen = function(event) {
                console.log("onOpen::" + JSON.stringify(event, null, 4));
            };

            this.webSocket.onmessage = function(event) {
                var message = event.data;
                console.log("onMessage::" + JSON.stringify(message, null, 4));
            };

            this.webSocket.onclose = function(event) {
                console.log("onClose::" + JSON.stringify(event, null, 4));
            };

            this.webSocket.onError = function(event) {
                console.log("onError::" + JSON.stringify(event, null, 4));
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
}


var client = new WebSocketClient(
    "ws",
    "localhost",
    8080,
    "/TestingServletsWithWebSockets_war_exploded/testsocket"
);
client.connect();