package com.danielohagan.webapp.businesslayer.entities.chat;

import com.danielohagan.webapp.businesslayer.entities.IEntity;
import com.danielohagan.webapp.businesslayer.entities.account.User;

import java.util.List;

public class ChatSession implements IEntity {

    private int mId;
    private List<User> mUserList;
    private List<Message> mMessageList;

    @Override
    public int getId() {
        return mId;
    }

    //TODO:: Getters and setters
}