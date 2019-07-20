package com.danielohagan.webapp.businesslayer.chat.websocket.attrributes;

public enum ClientActionEnum implements IAttribute {

    DISPLAY_MESSAGE_LIST("clientDisplayMessageList"),
    DISPLAY_MESSAGE("clientDisplayNewMessage"),
    DISPLAY_OLDER_MESSAGE("clientDisplayOlderMessage"),
    REMOVE_MESSAGE("clientRemoveMessage"),
    DISPLAY_MESSAGE_UPDATE("clientUpdateMessage"),

    DISPLAY_USER("clientDisplayUser"),
    REMOVE_USER("clientRemoveUser"),

    UPDATE_USER_PERMISSION("updateUserPermission"),

    SET_CHAT_SESSION_ID("clientSetChatSessionId"),
    REMOVE_CHAT_SESSION("clientRemoveChatSession"),
    ADD_CHAT_SESSION("clientAddChatSession"),
    DISPLAY_CHAT_SESSION("clientDisplayChatSession"),
    DISPLAY_CHAT_SESSION_NAME("clientDisplaySessionName"),
    UPDATE_CHAT_SESSION_NAME("clientUpdateSessionName");


    private String mActionString;

    ClientActionEnum(String actionString) {
        mActionString = actionString;
    }

    @Override
    public String toString() {
        return mActionString;
    }
}