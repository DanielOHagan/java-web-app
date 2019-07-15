package com.danielohagan.webapp.businesslayer.chat.websocket.json;

import com.danielohagan.webapp.businesslayer.chat.websocket.attrributes.AttributeEnum;
import com.danielohagan.webapp.businesslayer.chat.websocket.attrributes.ServerActionEnum;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class ChatMessageJsonDecoder {

    public Integer getUserId(String message) {
        Integer userId = null;

        /*
        Each message should contain a User ID
        identifying the sender of the message
         */

        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonObject = reader.readObject();

            if (jsonObject.containsKey(AttributeEnum.USER_ID.toString())) {
                userId = jsonObject.getInt(
                        AttributeEnum.USER_ID.toString()
                );
            }
        }

        return userId;
    }

    public Integer getTargetUserId(String message) {
        Integer newUserId;

        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonObject = reader.readObject();

            newUserId = jsonObject.getInt(AttributeEnum.TARGET_USER_ID.toString());
        }

        return newUserId;
    }

    public Integer getChatSessionId(String message) {
        Integer chatSessionId = null;

        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonObject = reader.readObject();

           if (jsonObject.containsKey(AttributeEnum.CHAT_SESSION_ID.toString())) {
               chatSessionId = jsonObject.getInt(
                       AttributeEnum.CHAT_SESSION_ID.toString()
               );
           }
        }

        return chatSessionId;
    }

    public ServerActionEnum decodeMessageAction(String message) {
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonObject = reader.readObject();

            String actionString = jsonObject.getString(ServerActionEnum.ACTION.toString());

            return parseChatActionEnumFromString(actionString);
        }
    }

    public ServerActionEnum parseChatActionEnumFromString(String stringValue) {
        if (stringValue != null && !stringValue.isEmpty()) {
            for (ServerActionEnum serverActionEnum : ServerActionEnum.values()) {
                if (serverActionEnum.toString().equals(stringValue)) {
                    return serverActionEnum;
                }
            }
        }

        return ServerActionEnum.NO_ACTION;
    }

    public Integer getMessageId(String message) {
        Integer messageId;

        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonObject = reader.readObject();

            messageId = jsonObject.getInt(AttributeEnum.ID.toString());
        }

        return messageId;
    }

    public String getMessageBody(String message) {
        String body = null;

        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonObject = reader.readObject();

            if (jsonObject.containsKey(AttributeEnum.BODY.toString())) {
                body = jsonObject.getString(
                        AttributeEnum.BODY.toString()
                );
            }
        }

        return body;
    }
}