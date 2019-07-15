package com.danielohagan.webapp.businesslayer.chat.websocket.json;

import com.danielohagan.webapp.businesslayer.chat.websocket.attrributes.AttributeEnum;
import com.danielohagan.webapp.businesslayer.chat.websocket.attrributes.ClientActionEnum;
import com.danielohagan.webapp.businesslayer.chat.websocket.attrributes.ServerActionEnum;
import com.danielohagan.webapp.businesslayer.entities.chat.ChatSessionUser;
import com.danielohagan.webapp.businesslayer.entities.chat.Message;
import com.danielohagan.webapp.datalayer.dao.implementations.UserDAOImpl;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import java.util.List;

public class ChatMessageJsonEncoder {

    public JsonObject generateUpdateUserPermission(ChatSessionUser user) {
        JsonProvider provider = JsonProvider.provider();
        return provider.createObjectBuilder()
                .add(
                        ServerActionEnum.ACTION.toString(),
                        ClientActionEnum.UPDATE_USER_PERMISSION.toString()
                )
                .add(
                        AttributeEnum.USER_ID.toString(),
                        user.getId()
                )
                .add(
                        AttributeEnum.USERNAME.toString(),
                        user.getUsername()
                )
                .add(
                        AttributeEnum.CHAT_SESSION_ID.toString(),
                        user.getChatSessionId()
                )
                .add(
                        AttributeEnum.PERMISSION_LEVEL.toString(),
                        user.getPermissionLevel().toString()
                )
                .build();
    }

    public JsonObject generateDeleteMessage(int messageId) {
        JsonProvider provider = JsonProvider.provider();
        return provider.createObjectBuilder()
                .add(
                        ServerActionEnum.ACTION.toString(),
                        ServerActionEnum.DELETE_MESSAGE.toString()
                )
                .add(
                        AttributeEnum.ID.toString(),
                        messageId
                )
                .build();
    }

    public JsonObject generateDisplayMessageJson(Message message, boolean isNewMessage) {
        JsonProvider provider = JsonProvider.provider();

        return provider.createObjectBuilder()
                .add(
                        ServerActionEnum.ACTION.toString(),
                        isNewMessage ?
                                ClientActionEnum.DISPLAY_MESSAGE.toString() :
                                ClientActionEnum.DISPLAY_OLDER_MESSAGE.toString()
                )
                .add(
                        AttributeEnum.MESSAGE.toString(),
                        generateChatMessageJson(message)
                )
                .build();
    }

    public JsonObject generateDisplayUserJson(ChatSessionUser user) {
        JsonProvider provider = JsonProvider.provider();

        return provider.createObjectBuilder()
                .add(
                        ServerActionEnum.ACTION.toString(),
                        ClientActionEnum.DISPLAY_USER.toString()
                )
                .add(
                        AttributeEnum.USERNAME.toString(),
                        user.getUsername()
                )
                .add(
                        AttributeEnum.ID.toString(),
                        user.getId()
                )
                .add(
                        AttributeEnum.STATUS.toString(),
                        user.getUserStatus().toString()
                )
                .add(
                        AttributeEnum.PERMISSION_LEVEL.toString(),
                        user.getPermissionLevel().toString()
                )
                .build();
    }

    public JsonObject generateMessageListAsJson(List<Message> messageList) {
        JsonObject messageListJson = null;

        if (
                messageList != null &&
                        messageList.size() > 0
        ) {
            JsonProvider provider = JsonProvider.provider();
            JsonArray messageArrayJson = JsonArray.EMPTY_JSON_ARRAY;

            for (Message message : messageList) {
                //TODO:: FIX THIS
//                messageArrayJson.add(generateChatMessageJson(message));
//                messageArrayJson.
            }

            messageListJson = provider.createObjectBuilder()
                    .add(
                            ServerActionEnum.ACTION.toString(),
                            ClientActionEnum.DISPLAY_MESSAGE_LIST.toString()
                    )
                    .add(
                            AttributeEnum.MESSAGE_LIST.toString(),
                            messageArrayJson
                    )
                    .build();
        }

        return messageListJson;
    }

    private JsonObject generateChatMessageJson(Message message) {
        JsonObject messageJson = null;

        if (message != null) {
            JsonProvider provider = JsonProvider.provider();

            messageJson = provider.createObjectBuilder()
                    .add(
                            AttributeEnum.ID.toString(),
                            message.getId()
                    )
                    .add(
                            AttributeEnum.BODY.toString(),
                            message.getBody()
                    )
                    .add(
                            AttributeEnum.SENDER_ID.toString(),
                            message.getSenderId()
                    )
                    .add(
                            AttributeEnum.SENDER_USERNAME.toString(),
                            new UserDAOImpl().getColumnString(
                                    message.getSenderId(),
                                    UserDAOImpl.USERNAME_COLUMN_NAME
                            )
                    )
                    .add(
                            AttributeEnum.CHAT_SESSION_ID.toString(),
                            message.getChatSessionId()
                    )
                    .add(
                            AttributeEnum.CREATION_TIME.toString(),
                            message.getCreationTime().toString()
                    )
                    .build();
        }

        return messageJson;
    }

    public JsonObject generateChatSessionName(String sessionName) {
        JsonProvider provider = JsonProvider.provider();

        return provider.createObjectBuilder()
                .add(
                        ServerActionEnum.ACTION.toString(),
                        ClientActionEnum.DISPLAY_CHAT_SESSION_NAME.toString()
                )
                .add(
                        AttributeEnum.CHAT_SESSION_NAME.toString(),
                        sessionName
                )
                .build();
    }

    public JsonObject generateAddChatSession(
            int chatSessionId,
            String chatSessionName
    ) {
        JsonProvider provider = JsonProvider.provider();

        return provider.createObjectBuilder()
                .add(
                        ServerActionEnum.ACTION.toString(),
                        ClientActionEnum.ADD_CHAT_SESSION.toString()
                )
                .add(
                        AttributeEnum.CHAT_SESSION_ID.toString(),
                        chatSessionId
                )
                .add(
                        AttributeEnum.CHAT_SESSION_NAME.toString(),
                        chatSessionName
                )
                .build();
    }
}