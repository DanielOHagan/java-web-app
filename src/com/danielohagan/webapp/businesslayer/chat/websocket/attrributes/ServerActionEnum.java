package com.danielohagan.webapp.businesslayer.chat.websocket.attrributes;

import com.danielohagan.webapp.datalayer.dao.databaseenums.ChatPermissionLevel;

import java.util.*;

public enum ServerActionEnum implements IAttribute {

    ADD_MESSAGE("addMessage"),
    DELETE_MESSAGE("deleteMessage"),
    EDIT_MESSAGE("editMessage"),

    ADD_USER("addUser"),
    REMOVE_USER("removeUser"),

    DELETE_CHAT_SESSION("deleteChatSession"),
    CREATE_NEW_CHAT_SESSION("createNewChatSession"),

    CHANGE_USER_PERMISSION("changeUserPermission"),

    INFO("info"),
    ERROR("error"),

    INIT("init"),
    CLOSE("close"),
    CLOSE_PREVIOUS_CHAT_SESSION("closePreviousChat"),

    NO_ACTION("noAction"),
    ACTION("action");

    private String mActionString;

    ServerActionEnum(String actionString) {
        mActionString = actionString;
    }

    @Override
    public String toString() {
        return mActionString;
    }

    /*
    Lists containing the required User Permissions of Server Actions
     */
    public static class PermissionLists {
        private static EnumMap<ServerActionEnum, List<ChatPermissionLevel>> mActionPermissionLevelMap;

        static {
            mActionPermissionLevelMap = new EnumMap<>(ServerActionEnum.class);

            mActionPermissionLevelMap.put(
                    ServerActionEnum.ADD_MESSAGE,
                    Arrays.asList(
                            ChatPermissionLevel.CREATOR,
                            ChatPermissionLevel.ADMIN,
                            ChatPermissionLevel.MEMBER
                    )
            );

            mActionPermissionLevelMap.put(
                    ServerActionEnum.DELETE_MESSAGE,
                    Arrays.asList(
                            ChatPermissionLevel.CREATOR,
                            ChatPermissionLevel.ADMIN
                    )
            );

            mActionPermissionLevelMap.put(
                    ServerActionEnum.EDIT_MESSAGE,
                    Arrays.asList(
                            ChatPermissionLevel.CREATOR,
                            ChatPermissionLevel.ADMIN,
                            ChatPermissionLevel.MEMBER
                    )
            );

            mActionPermissionLevelMap.put(
                    ServerActionEnum.DELETE_CHAT_SESSION,
                    Arrays.asList(
                            ChatPermissionLevel.CREATOR
                    )
            );

            mActionPermissionLevelMap.put(
                    ServerActionEnum.CHANGE_USER_PERMISSION,
                    Arrays.asList(
                            ChatPermissionLevel.CREATOR,
                            ChatPermissionLevel.ADMIN
                    )
            );

            mActionPermissionLevelMap.put(
                    ServerActionEnum.ADD_USER,
                    Arrays.asList(
                            ChatPermissionLevel.CREATOR,
                            ChatPermissionLevel.ADMIN,
                            ChatPermissionLevel.MEMBER
                    )
            );

            mActionPermissionLevelMap.put(
                    ServerActionEnum.REMOVE_USER,
                    Arrays.asList(
                            ChatPermissionLevel.CREATOR,
                            ChatPermissionLevel.ADMIN
                    )
            );
        }

        public static boolean hasPermission(
                ServerActionEnum action,
                ChatPermissionLevel permissionLevel
        ) {
            if (mActionPermissionLevelMap.get(action) != null) {
                return mActionPermissionLevelMap.get(action).contains(permissionLevel);
            }

            return false;
        }
    }
}