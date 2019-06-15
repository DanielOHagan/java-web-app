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

public class ChatJsonBuilder {

    public JsonObject generateUpdateUserPermission(ChatSessionUser user) {
        JsonProvider provider = JsonProvider.provider();
        return provider.createObjectBuilder()
                .add(
                        ServerActionEnum.ACTION.getAttributeString(),
                        ClientActionEnum.UPDATE_USER_PERMISSION.getAttributeString()
                )
                .add(
                        AttributeEnum.USER_ID.getAttributeString(),
                        user.getId()
                )
                .add(
                        AttributeEnum.USERNAME.getAttributeString(),
                        user.getUsername()
                )
                .add(
                        AttributeEnum.CHAT_SESSION_ID.getAttributeString(),
                        user.getChatSessionId()
                )
                .add(
                        AttributeEnum.PERMISSION_LEVEL.getAttributeString(),
                        user.getPermissionLevel().getDatabaseEnumStringValue()
                )
                .build();
    }

    public JsonObject generateDeleteMessage(int messageId) {
        JsonProvider provider = JsonProvider.provider();
        return provider.createObjectBuilder()
                .add(
                        ServerActionEnum.ACTION.getAttributeString(),
                        ServerActionEnum.DELETE_MESSAGE.getAttributeString()
                )
                .add(
                        AttributeEnum.ID.getAttributeString(),
                        messageId
                )
                .build();
    }

    public JsonObject generateDisplayMessageJson(Message message, boolean isNewMessage) {
        JsonProvider provider = JsonProvider.provider();

        return provider.createObjectBuilder()
                .add(
                        ServerActionEnum.ACTION.getAttributeString(),
                        isNewMessage ?
                                ClientActionEnum.DISPLAY_MESSAGE.getAttributeString() :
                                ClientActionEnum.DISPLAY_OLDER_MESSAGE.getAttributeString()
                )
                .add(
                        AttributeEnum.MESSAGE.getAttributeString(),
                        generateChatMessageJson(message)
                )
                .build();
    }

    public JsonObject generateDisplayUserJson(ChatSessionUser user) {
        JsonProvider provider = JsonProvider.provider();

        return provider.createObjectBuilder()
                .add(
                        ServerActionEnum.ACTION.getAttributeString(),
                        ClientActionEnum.DISPLAY_USER.getAttributeString()
                )
                .add(
                        AttributeEnum.USERNAME.getAttributeString(),
                        user.getUsername()
                )
                .add(
                        AttributeEnum.ID.getAttributeString(),
                        user.getId()
                )
                .add(
                        AttributeEnum.STATUS.getAttributeString(),
                        user.getUserStatus().getDatabaseEnumStringValue()
                )
                .add(
                        AttributeEnum.PERMISSION_LEVEL.getAttributeString(),
                        user.getPermissionLevel().getDatabaseEnumStringValue()
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
                            ServerActionEnum.ACTION.getAttributeString(),
                            ClientActionEnum.DISPLAY_MESSAGE_LIST.getAttributeString()
                    )
                    .add(
                            AttributeEnum.MESSAGE_LIST.getAttributeString(),
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
                            AttributeEnum.ID.getAttributeString(),
                            message.getId()
                    )
                    .add(
                            AttributeEnum.BODY.getAttributeString(),
                            message.getBody()
                    )
                    .add(
                            AttributeEnum.SENDER_ID.getAttributeString(),
                            message.getSenderId()
                    )
                    .add(
                            AttributeEnum.SENDER_USERNAME.getAttributeString(),
                            new UserDAOImpl().getColumnString(
                                    message.getSenderId(),
                                    UserDAOImpl.USERNAME_COLUMN_NAME
                            )
                    )
                    .add(
                            AttributeEnum.CHAT_SESSION_ID.getAttributeString(),
                            message.getChatSessionId()
                    )
                    .add(
                            AttributeEnum.CREATION_TIME.getAttributeString(),
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
                        ServerActionEnum.ACTION.getAttributeString(),
                        ClientActionEnum.DISPLAY_CHAT_SESSION_NAME.getAttributeString()
                )
                .add(
                        AttributeEnum.CHAT_SESSION_NAME.getAttributeString(),
                        sessionName
                )
                .build();
    }
}