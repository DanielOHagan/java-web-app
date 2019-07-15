package com.danielohagan.webapp.businesslayer.entities.chat;

import com.danielohagan.webapp.businesslayer.entities.IEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
            LocalDateTime creationTime
    ) {
        mId = id;
        mName = name;
        mUserList = userList;
        mCreationTime = creationTime;
        mMessageList = new ArrayList<>();
    }

    public ChatSession(
            int id,
            String name,
            LocalDateTime creationTime
    ) {
        mId = id;
        mName = name;
        mCreationTime = creationTime;
        mMessageList = new ArrayList<>();
        mUserList = new ArrayList<>();
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

    public LocalDateTime getCreationTime() {
        return mCreationTime;
    }

    public Message getMessageById(int messageId) {
        if (mMessageList != null) {
            for (Message message : mMessageList) {
                if (message.getId() == messageId) {
                    return message;
                }
            }
        }

        return null;
    }
}