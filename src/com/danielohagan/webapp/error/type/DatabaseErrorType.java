package com.danielohagan.webapp.error.type;

import com.danielohagan.webapp.error.ErrorSeverity;

public enum  DatabaseErrorType implements IErrorType {

    /* Database Errors */
    DATABASE_CONNECTION_FAILED("Could not connect to DataBase", true, ErrorSeverity.FATAL),
    FAILED_TO_RETRIEVE_TABLE("Table can not be found", true, ErrorSeverity.FATAL);

    private String mErrorMessage;
    private boolean mHideFromUser;
    private ErrorSeverity mErrorSeverity;

    DatabaseErrorType(
            String errorMessage,
            boolean hideFromUser,
            ErrorSeverity errorSeverity
    ) {
        mErrorMessage = errorMessage;
        mHideFromUser = hideFromUser;
        mErrorSeverity = errorSeverity;
    }

    @Override
    public String getErrorMessage() {
        return mErrorMessage;
    }

    @Override
    public boolean shouldHideFromUser() {
        return mHideFromUser;
    }

    @Override
    public ErrorSeverity getErrorSeverity() {
        return mErrorSeverity;
    }
}