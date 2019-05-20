package com.danielohagan.webapp.businesslayer.entities.chat;

import com.danielohagan.webapp.businesslayer.entities.IEntity;

import java.time.LocalDateTime;

public class Message implements IEntity {

    private int mId;
    private String mMessageBody;
    private LocalDateTime mCreationTime;

    public Message() {}

    public Message(int id, String messageBody, LocalDateTime creationTime) {
        mId = id;
        mMessageBody = messageBody;
        mCreationTime = creationTime;
    }

    @Override
    public int getId() {
        return mId;
    }

    //TODO: Getters and setters
}