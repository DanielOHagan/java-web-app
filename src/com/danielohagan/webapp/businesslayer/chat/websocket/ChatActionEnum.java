package com.danielohagan.webapp.businesslayer.chat.websocket;

public enum ChatActionEnum {

    ADD_MESSAGE("addMessage"),
    DELETE_MESSAGE("deleteMessage"),
    EDIT_MESSAGE("editMessage"),

    ADD_USER("addUser"),
    REMOVE_USER("removeUser"),

    DELETE_CHAT_SESSION("deleteChatSession"),

    INFO("info"),
    ERROR("error"),

    INIT("init"),
    CLOSE("close"),

    DISPLAY_MESSAGE_LIST("displayMessageList"),
    DISPLAY_MESSAGE("displayMessage"),
    DISPLAY_USER("displayUser"),

    NO_ACTION("noAction"),
    ACTION("action");

    private String mActionString;

    ChatActionEnum(String actionString) {
        mActionString = actionString;
    }

    public String getActionString() {
        return mActionString;
    }
}