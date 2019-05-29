package com.danielohagan.webapp.error.type;

import com.danielohagan.webapp.datalayer.dao.implementations.UserDAOImpl;
import com.danielohagan.webapp.error.ErrorSeverity;

public enum AccountErrorType implements IErrorType {

    NO_USER_ID("No User ID was given", false, ErrorSeverity.MINOR),
    DOES_NOT_EXIST(
            "The requested account does not exist",
            false,
            ErrorSeverity.MINOR
    ),
    DOES_NOT_HAVE_PERMISSION(
            "Your account does not have the required permission to perform the task",
            false,
            ErrorSeverity.MINOR
    ),

    FAILED_TO_RETRIEVE_USER(
            "Failed to retrieve account information",
            false,
            ErrorSeverity.MINOR
    ),
    FAILED_TO_RETRIEVE_EMAIL(
            "Failed to retrieve account Email",
            false,
            ErrorSeverity.MINOR
    ),
    FAILED_TO_RETRIEVE_USERNAME(
            "Failed to retrieve account Username",
            false,
            ErrorSeverity.MINOR
    ),
    ID_NOT_NEGATIVE(
            "The User ID must not be negative",
            false,
            ErrorSeverity.MINOR
    ),
    ID_NUMERIC_ONLY(
            "User ID must be a numerical value",
            false,
            ErrorSeverity.MINOR
    ),


    /* ACCOUNT CREATION */
    CREATION_FAILED(
            "Account creation has failed",
            false,
            ErrorSeverity.MAJOR
    ),
    SUCCESSFUL_CREATION(
            "Successfully created an account",
            false,
            ErrorSeverity.INFO
    ),
    /* ACCOUNT CREATION EMAIL */
    CREATION_EMAIL_TAKEN(
            "Failed to create account: Email taken",
            false,
            ErrorSeverity.MINOR
    ),


    /* ACCOUNT CREATION USERNAME */
    CREATION_USERNAME_TAKEN(
            "Failed to create account: Username taken",
            false,
            ErrorSeverity.INFO
    ),

    /* ACCOUNT CREATION PASSWORD */


    /* ACCOUNT DATA UPDATING */
    CHANGE_PASSWORD_SUCCESS(
            "Password successfully changed",
            false,
            ErrorSeverity.INFO
    ),


    /* USER INPUT */
    /* USER INPUT EMAIL */
    INPUT_EMAIL_INCORRECT("Incorrect email", false, ErrorSeverity.MINOR),
    INPUT_EMAIL_EMPTY("The email input field was empty", false, ErrorSeverity.MINOR),
    INPUT_EMAIL_TOO_LONG(
            "The email input is longer than the maximum, max: " +
            UserDAOImpl.EMAIL_MAX_LENGTH +
            " characters",
            false,
            ErrorSeverity.MINOR
    ),
    INPUT_EMAIL_TOO_SHORT(
            "The email input is shorter than the minimum, min: " +
            UserDAOImpl.EMAIL_MIN_LENGTH +
            " characters",
            false,
            ErrorSeverity.MINOR
    ),
    INPUT_EMAIL_MISMATCH("The input Email addresses do not match", false, ErrorSeverity.MINOR),

    /* USER INPUT USERNAME */
    INPUT_USERNAME_EMPTY("The username input field was empty", false, ErrorSeverity.MINOR),
    INPUT_USERNAME_TOO_LONG(
            "The username input is longer than the maximum, max: " +
            UserDAOImpl.USERNAME_MAX_LENGTH +
            " characters",
            false,
            ErrorSeverity.MINOR
    ),
    INPUT_USERNAME_TOO_SHORT(
            "The username input is shorter than the minimum, min: " +
                    UserDAOImpl.USERNAME_MIN_LENGTH + " characters",
            false,
            ErrorSeverity.MINOR
    ),

    /* USER INPUT PASSWORD */
    INPUT_PASSWORD_INCORRECT("Incorrect password", false, ErrorSeverity.MINOR),
    INPUT_PASSWORD_EMPTY("The password input field was empty", false, ErrorSeverity.MINOR),
    INPUT_PASSWORD_TOO_LONG(
            "The password input is longer than the maximum, max: " +
            UserDAOImpl.PASSWORD_MAX_LENGTH +
            " characters",
            false,
            ErrorSeverity.MINOR
    ),
    INPUT_PASSWORD_TOO_SHORT(
            "The password input is shorter than the minimum, min: " +
            UserDAOImpl.PASSWORD_MIN_LENGTH +
            " characters",
            false,
            ErrorSeverity.MINOR
    ),
    INPUT_PASSWORD_CONTAINS_INVALID_CHARACTER(
            "The password input contains an invalid character. The characters: " +
                    "" + //TODO:: Add the illegal characters
                    " can not be used is the password",
            false,
            ErrorSeverity.MINOR
    ),
    INPUT_PASSWORD_MISMATCH("The input passwords do not match", false, ErrorSeverity.MINOR),


    /* ACCOUNT LOGIN/LOGOUT */
    LOGIN_NO_CORRESPONDING_ACCOUNT("There is no account corresponding to the given inputs", false, ErrorSeverity.MINOR),
    NOT_LOGGED_IN("Not currently logged in", false, ErrorSeverity.MINOR),
    ALREADY_LOGGED_IN("You are already logged in", false, ErrorSeverity.MINOR),
    SUCCESSFUL_LOG_OUT("Successfully logged out of your account", false, ErrorSeverity.INFO),
    LOGIN_FAILED("Failed to login", false, ErrorSeverity.INFO),

    /* ACCOUNT DELETION */
    DELETION_FAILED("Failed to delete account", false, ErrorSeverity.MAJOR),


    INVALID_EMAIL("Invalid Email address", false, ErrorSeverity.MINOR),
    INVALID_USERNAME("Invalid Username", false, ErrorSeverity.MINOR);


    private String mErrorMessage;
    private boolean mHideFromUser;
    private ErrorSeverity mErrorSeverity;

    AccountErrorType(
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

    public void setErrorSeverity(ErrorSeverity mErrorSeverity) {
        this.mErrorSeverity = mErrorSeverity;
    }
}