package com.danielohagan.webapp.businesslayer.entities.chat;

import com.danielohagan.webapp.businesslayer.entities.IEntity;

import java.time.LocalDateTime;
import java.util.List;

public class ChatSession implements IEntity {

    private int mId;
    private String mName;
    private List<ChatSessionUser> mUserList;
    private List<Message> mMessageList;
    private LocalDateTime mCreationTime;

    public ChatSession() {}

    public ChatSession(
            int id,
            String name,
            List<ChatSessionUser> userList,
//            List<Message> messageList,
            LocalDateTime creationTime
    ) {
        mId = id;
        mName = name;
        mUserList = userList;
//        mMessageList = messageList;
        mCreationTime = creationTime;
    }

    public ChatSession(
            int id,
            String name,
//            List<Message> messageList,
            LocalDateTime creationTime
    ) {
        mId = id;
        mName = name;
//        mMessageList = messageList;
        mCreationTime = creationTime;
    }

    @Override
    public int getId() {
        return mId;
    }

    public List<Message> getMessageList() {
        return mMessageList;
    }

    public void setMessageList(List<Message> messageList) {
        mMessageList = messageList;
    }

    public String getName() {
        return mName;
    }

    public List<ChatSessionUser> getUserList() {
        return mUserList;
    }

    public void setUserList(List<ChatSessionUser> userList) {
        mUserList = userList;
    }

    //TODO:: Getters and setters
}