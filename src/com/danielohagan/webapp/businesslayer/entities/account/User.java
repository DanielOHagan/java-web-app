package com.danielohagan.webapp.businesslayer.entities.account;

import com.danielohagan.webapp.businesslayer.entities.IEntity;
import com.danielohagan.webapp.datalayer.dao.databaseenums.UserStatus;

import java.time.LocalDateTime;

public class User implements IEntity {

    private int mId;
    private String mEmail;
    private String mUsername;
    private UserStatus mUserStatus;
    private LocalDateTime mCreationTime;

    public User() {}

    public User(
            int id,
            String email,
            String username,
            UserStatus userStatus,
            LocalDateTime creationTime
    ) {
        mId = id;
        mEmail = email;
        mUsername = username;
        mUserStatus = userStatus;
        mCreationTime = creationTime;
    }

    @Override
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
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

    public UserStatus getUserStatus() {
        return mUserStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        mUserStatus = userStatus;
    }

    public LocalDateTime getCreationTime() {
        return mCreationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        mCreationTime = creationTime;
    }

}