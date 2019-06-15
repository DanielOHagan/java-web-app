package com.danielohagan.webapp.businesslayer.chat.websocket;

import com.danielohagan.webapp.businesslayer.chat.websocket.attrributes.AttributeEnum;
import com.danielohagan.webapp.businesslayer.chat.websocket.attrributes.ServerActionEnum;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class WebSocketMessageUtils {

    public static Integer getUserId(String message) {
        Integer userId = null;

        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonObject = reader.readObject();

            userId = jsonObject.getInt(AttributeEnum.USER_ID.getAttributeString());
        }

        return userId;
    }

    public static Integer getNewUserId(String message) {
        Integer newUserId;

        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonObject = reader.readObject();

            newUserId = jsonObject.getInt(AttributeEnum.NEW_USER_ID.getAttributeString());
        }

        return newUserId;
    }

    public static Integer getChatSessionId(String message) {
        Integer chatSessionId;

        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonObject = reader.readObject();

            chatSessionId = jsonObject.getInt(AttributeEnum.CHAT_SESSION_ID.getAttributeString());
        }

        return chatSessionId;
    }

    public static ServerActionEnum decodeMessageAction(String message) {
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonObject = reader.readObject();

            String actionString = jsonObject.getString(ServerActionEnum.ACTION.getAttributeString());

            return parseChatActionEnumFromString(actionString);
        }
    }

    public static ServerActionEnum parseChatActionEnumFromString(String stringValue) {
        if (stringValue != null && !stringValue.isEmpty()) {
            for (ServerActionEnum serverActionEnum : ServerActionEnum.values()) {
                if (serverActionEnum.getAttributeString().equals(stringValue)) {
                    return serverActionEnum;
                }
            }
        }

        return ServerActionEnum.NO_ACTION;
    }

    public static Integer getMessageId(String message) {
        Integer messageId;

        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonObject = reader.readObject();

            messageId = jsonObject.getInt(AttributeEnum.ID.getAttributeString());
        }

        return messageId;
    }
}