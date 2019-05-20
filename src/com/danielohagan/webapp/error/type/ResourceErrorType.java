package com.danielohagan.webapp.error.type;

public enum ResourceErrorType implements IErrorType {

    NOT_FOUND("The requested resource can not be found or does not exist.", false);

    private String mErrorMessage;
    private boolean mHideFromUser;

    ResourceErrorType(String errorMessage, boolean hideFromUser) {
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