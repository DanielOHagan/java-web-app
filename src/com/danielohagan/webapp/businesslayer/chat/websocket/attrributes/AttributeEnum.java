package com.danielohagan.webapp.businesslayer.chat.websocket.attrributes;

public enum AttributeEnum implements IAttribute {

    ID("id"),
    BODY("body"),
    CHAT_SESSION_ID("chatSessionId"),
    SENDER_ID("senderId"),
    CREATION_TIME("creationTime"),

    MESSAGE_LIST("messageList"),
    MESSAGE("message"),

    SENDER_USERNAME("senderUsername"),

    CHAT_SESSION_NAME("chatSessionName"),

    USER_ID("userId"),
    NEW_USER_ID("newUserId"),
    USERNAME("userUsername"),
    STATUS("userStatus"),
    PERMISSION_LEVEL("permissionLevel");

    private String mAttributeString;

    AttributeEnum(String attributeString) {
        mAttributeString = attributeString;
    }

    @Override
    public String getAttributeString() {
        return mAttributeString;
    }

}