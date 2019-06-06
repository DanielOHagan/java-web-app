package com.danielohagan.webapp.businesslayer.chat.websocket.EntityAttributeEnum;

public enum MessageEntityAttributeEnum implements IEntityAttributeEnum {

    ID("id"),
    BODY("body"),
    CHAT_SESSION_ID("chatSessionId"),
    SENDER_ID("senderId"),
    CREATION_TIME("creationTime"),

    MESSAGE_LIST("messageList"),
    MESSAGE("message"),

    SENDER_USERNAME("senderUsername");

    private String mAttributeString;

    MessageEntityAttributeEnum(String attributeString) {
        mAttributeString = attributeString;
    }

    @Override
    public String getAttributeString() {
        return mAttributeString;
    }
}