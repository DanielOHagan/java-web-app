package com.danielohagan.webapp.error.type;

import com.danielohagan.webapp.error.ErrorSeverity;

public enum SessionErrorType implements IErrorType {

    FAILED_TO_RETRIEVE_CURRENT_USER(
            "Failed to retrieve the session's current User.",
            false,
            ErrorSeverity.MAJOR
    );

    private String mErrorMessage;
    private boolean mHideFromUser;
    private ErrorSeverity mErrorSeverity;

    SessionErrorType(String errorMessage, boolean hideFromUser, ErrorSeverity errorSeverity) {
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