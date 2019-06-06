package com.danielohagan.webapp.businesslayer.chat.websocket;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class WebSocketMessageUtils {

    public static Integer getUserId(String message) {
        Integer userId = null;

        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonObject = reader.readObject();

            userId = jsonObject.getInt("userId");
        }

        return userId;
    }

    public static Integer getChatSessionId(String message) {
        Integer chatSessionId;

        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonObject = reader.readObject();

            chatSessionId = jsonObject.getInt("chatSessionId");
        }

        return chatSessionId;
    }

    public static ChatActionEnum decodeMessageAction(String message) {
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonObject = reader.readObject();

            String actionString = jsonObject.getString(ChatActionEnum.ACTION.getActionString());

            return parseChatActionEnumFromString(actionString);
        }
    }

    public static ChatActionEnum parseChatActionEnumFromString(String stringValue) {
        if (stringValue != null && !stringValue.isEmpty()) {
            for (ChatActionEnum chatActionEnum : ChatActionEnum.values()) {
                if (chatActionEnum.getActionString().equals(stringValue)) {
                    return chatActionEnum;
                }
            }
        }

        return ChatActionEnum.NO_ACTION;
    }
}