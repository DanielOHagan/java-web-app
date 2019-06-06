package com.danielohagan.webapp.businesslayer.entities.chat;

import com.danielohagan.webapp.businesslayer.entities.IEntity;

import java.time.LocalDateTime;

public class Message implements IEntity {

    private int mId;
    private int mSenderId;
    private int mChatSessionId;
    private String mBody;
    private LocalDateTime mCreationTime;

    public Message() {}

    public Message(
            int id,
            int senderId,
            int chatSessionId,
            String body,
            LocalDateTime creationTime
    ) {
        mId = id;
        mSenderId = senderId;
        mChatSessionId = chatSessionId;
        mBody = body;
        mCreationTime = creationTime;
    }

    @Override
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setSenderId(int senderId) {
        mSenderId = senderId;
    }

    public int getSenderId() {
        return mSenderId;
    }

    public void setChatSessionId(int chatSessionId) {
        mChatSessionId = chatSessionId;
    }

    public int getChatSessionId() {
        return mChatSessionId;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public String getBody() {
        return mBody;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        mCreationTime = creationTime;
    }

    public LocalDateTime getCreationTime() {
        return mCreationTime;
    }
}