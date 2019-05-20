package com.danielohagan.webapp.error.type;

public enum  DatabaseErrorType implements IErrorType {

    /* Database Errors */
    DATABASE_CONNECTION_FAILED("Could not connect to DataBase", true),
    FAILED_TO_RETRIEVE_TABLE("Table can not be found", true);

    private String mErrorMessage;
    private boolean mHideFromUser;

    DatabaseErrorType(String errorMessage, boolean hideFromUser) {
        mErrorMessage = errorMessage;
        mHideFromUser = hideFromUser;
    }

    @Override
    public String getErrorMessage() {
        return mErrorMessage;
    }

    @Override
    public boolean shouldHideFromUser() {
        return mHideFromUser;
    }
}