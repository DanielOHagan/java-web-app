package com.danielohagan.webapp.businesslayer.entities.account;

import com.danielohagan.webapp.businesslayer.entities.IEntity;
import com.danielohagan.webapp.businesslayer.entities.chat.ChatSession;

import java.util.List;

public class User implements IEntity {

    private int mId;
    private String mEmail;
    private String mUsername;
    private List<ChatSession> mChatSessionList;

    public User() {}

    public User(int id, String email, String username) {
        mId = id;
        mEmail = email;
        mUsername = username;
    }

    @Override
    public int getId() {
        return mId;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public void setUsername(String username) {
        mUsername = username;
    }
}