package com.danielohagan.webapp.businesslayer.chat.websocket.attrributes;

public enum AttributeEnum implements IAttribute {

    ID("id"),
    BODY("body"),
    SENDER_ID("senderId"),
    CREATION_TIME("creationTime"),

    MESSAGE_LIST("messageList"),
    MESSAGE_ID("messageId"),
    MESSAGE("message"),

    SENDER_USERNAME("senderUsername"),

    CHAT_SESSION_ID("chatSessionId"),
    CHAT_SESSION_NAME("chatSessionName"),

    USER_ID("userId"),
    TARGET_USER_ID("targetUserId"),
    USERNAME("userUsername"),
    STATUS("userStatus"),
    PERMISSION_LEVEL("permissionLevel");

    private String mAttributeString;

    AttributeEnum(String attributeString) {
        mAttributeString = attributeString;
    }

    @Override
    public String toString() {
        return mAttributeString;
    }

}