package com.danielohagan.webapp.error;

public enum ApplicationControllerErrorType implements IErrorType {

    COMMAND_CLASS_NOT_FOUND("Can not find the requested command class", true);

    private String mErrorMessage;
    private boolean mHideFromUser;

    ApplicationControllerErrorType(
            String errorMessage,
            boolean hideFromUser
    ) {
        mErrorMessage = errorMessage;
        mHideFromUser = hideFromUser;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }

    @Override
    public boolean shouldHideFromUser() {
        return mHideFromUser;
    }
}
