package com.danielohagan.webapp.businesslayer.entities.chat;

import com.danielohagan.webapp.businesslayer.entities.account.User;
import com.danielohagan.webapp.datalayer.dao.databaseenums.ChatPermissionLevel;

public class ChatSessionUser extends User {

    /* Extends User and provides attributes used only when in a Chat Session */

    private ChatPermissionLevel mPermissionLevel;
    private int mChatSessionId;

    public ChatSessionUser() {}

    public ChatSessionUser(
            User user,
            int chatSessionId,
            ChatPermissionLevel permissionLevel
    ) {
        super(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getUserStatus(),
                user.getCreationTime()
        );
        mChatSessionId = chatSessionId;
        mPermissionLevel = permissionLevel;
    }

    public void setPermisisionLevel(ChatPermissionLevel permissionLevel) {
        mPermissionLevel = permissionLevel;
    }

    public ChatPermissionLevel getPermissionLevel() {
        return mPermissionLevel;
    }

    public int getChatSessionId() {
        return mChatSessionId;
    }
}