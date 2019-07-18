package com.danielohagan.webapp.error.type;

import com.danielohagan.webapp.error.ErrorSeverity;

public enum ChatErrorType implements IErrorType {

    UNABLE_TO_RETRIEVE_REQUIRED_ID(
            "Unable to retrieve the required ID(s) to perform task",
            false,
            ErrorSeverity.MAJOR
    ),
    FAILED_TO_RETRIEVE_SESSION(
            "Failed to retrieve Chat Session",
            false,
            ErrorSeverity.MAJOR
    ),
    INVALID_ID("Invalid Chat Session ID", false, ErrorSeverity.MAJOR),
    ACCOUNT_DOES_NOT_HAVE_ACCESS_TO_SESSION(
            "Your account is unable to access the requested Chat Session",
            false,
            ErrorSeverity.MAJOR
    ),
    NO_SESSION_ID("A Chat Session has not been given", false, ErrorSeverity.INFO),
    MESSAGE_DOES_NOT_EXIST("The selected message does not exist", false, ErrorSeverity.MAJOR),
    PERMISSION_CHECK_FAILED_SESSION_DELETION(
            "Your account does not have permission to delete the Chat Session",
            false,
            ErrorSeverity.MAJOR
    ),
    PERMISSION_CHECK_FAILED_UPDATE_MESSAGE(
            "Your account does not have permission to update that message",
            false,
            ErrorSeverity.MAJOR
    ),
    PERMISSION_CHECK_FAILED_ADD_USER(
            "Your account does not have permission to add a new user",
            false,
            ErrorSeverity.MAJOR
    ),
    PERMISSION_CHECK_FAILED_REMOVE_USER(
            "Your account does not have permission to remove a user",
            false,
            ErrorSeverity.MAJOR
    ),
    PERMISSION_CHECK_FAILED_DELETE_MESSAGE(
            "Your account does not have permission to delete the selected message",
            false,
            ErrorSeverity.MAJOR
    ),
    PERMISSION_CHECK_FAILED_DELETE_CHAT_SESSION(
            "Your account does not have permission to delete the Chat Session",
            false,
            ErrorSeverity.MAJOR
    ),
    SESSION_DOES_NOT_EXIST(
            "The request Chat Session does not exist",
            false,
            ErrorSeverity.MAJOR
    ),
    SESSION_ID_NOT_NEGATIVE(
            "Chat Session ID must not be negative",
            false,
            ErrorSeverity.MAJOR
    ),
    SESSION_ID_NUMERIC_ONLY(
            "Chat Session ID must be an integer",
            false,
            ErrorSeverity.MAJOR
    ),
    USER_ID_NOT_FOUND("Unable to retrieve User ID", false, ErrorSeverity.MAJOR),
    USER_NOT_IN_SESSION("Target User ID is not in session", false, ErrorSeverity.MINOR),
    CHAT_SESSION_ID_NOT_FOUND("Unable to retrieve Chat Session ID", false, ErrorSeverity.MAJOR),
    NEW_USER_NOT_FOUND("Unable to find New User", false, ErrorSeverity.MAJOR),
    NEW_USER_ALREADY_IN_SESSION("New User is already in Chat Session", false, ErrorSeverity.MINOR),
    CANT_REMOVE_CREATOR("Can't remove the creator of the Chat Session", false, ErrorSeverity.MINOR);

    private String mErrorMessage;
    private boolean mHideFromUser;
    private ErrorSeverity mErrorSeverity;

    ChatErrorType(
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