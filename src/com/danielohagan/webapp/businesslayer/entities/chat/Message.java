package com.danielohagan.webapp.businesslayer.entities.chat;

import com.danielohagan.webapp.businesslayer.entities.IEntity;

import java.time.LocalDate;

public class Message implements IEntity {

    private int mId;
    private String mMessageBody;
    private LocalDate mUploadTime;

    public Message() {}

    public Message(int id, String messageBody, LocalDate uploadTime) {
        mId = id;
        mMessageBody = messageBody;
        mUploadTime = uploadTime;
    }

    @Override
    public int getId() {
        return mId;
    }

    //TODO: Getters and setters
}