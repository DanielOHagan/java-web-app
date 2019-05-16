package com.danielohagan.webapp.error;

public enum SessionErrorType implements IErrorType {

    FAILED_TO_RETRIEVE_CURRENT_USER("Failed to retrieve the session's current User.", false);

    private String mErrorMessage;
    private boolean mHideFromUser;

    SessionErrorType(String errorMessage, boolean hideFromUser) {
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