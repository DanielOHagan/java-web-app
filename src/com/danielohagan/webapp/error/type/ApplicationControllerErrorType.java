package com.danielohagan.webapp.error.type;

import com.danielohagan.webapp.error.ErrorSeverity;

public enum ApplicationControllerErrorType implements IErrorType {

    COMMAND_CLASS_NOT_FOUND(
            "Can not find the requested command class",
            true,
            ErrorSeverity.FATAL
    );

    private String mErrorMessage;
    private boolean mHideFromUser;
    private ErrorSeverity mErrorSeverity;

    ApplicationControllerErrorType(
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
