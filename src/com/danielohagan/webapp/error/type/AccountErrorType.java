package com.danielohagan.webapp.error.type;

import com.danielohagan.webapp.datalayer.dao.implementations.UserDAOImpl;

public enum AccountErrorType implements IErrorType {

    /* ACCOUNT ERRORS */


    DOES_NOT_EXIST("The requested account does not exist", false),
    DOES_NOT_HAVE_PERMISSION("Your account does not have the required permission to perform the task", false),

    FAILED_TO_RETRIEVE_USER("Failed to retrieve account information", false),
    FAILED_TO_RETRIEVE_EMAIL("Failed to retrieve account Email", false),
    FAILED_TO_RETRIEVE_USERNAME("Failed to retrieve account Username", false),


    /* ACCOUNT CREATION ERRORS */
    CREATION_FAILED("Account creation has failed", false),
    /* ACCOUNT CREATION EMAIL ERRORS */
    CREATION_EMAIL_TAKEN("Failed to create account: Email taken", false),


    /* ACCOUNT CREATION USERNAME ERRORS */
    CREATION_USERNAME_TAKEN("Failed to create account: Username taken", false),

    /* ACCOUNT CREATION PASSWORD ERRORS */




    /* USER INPUT ERRORS */
    /* USER INPUT EMAIL ERRORS*/
    INPUT_EMAIL_INCORRECT("Incorrect email", false),
    INPUT_EMAIL_EMPTY("The email input field was empty", false),
    INPUT_EMAIL_TOO_LONG(
            "The email input is longer than the maximum, max: " +
            UserDAOImpl.EMAIL_MAX_LENGTH +
            " characters",
            false
    ),
    INPUT_EMAIL_TOO_SHORT(
            "The email input is shorter than the minimum, min: " +
            UserDAOImpl.EMAIL_MIN_LENGTH +
            " characters",
            false
    ),
    INPUT_EMAIL_MISMATCH("The input Email addresses do not match", false),

    /* USER INPUT USERNAME ERRORS*/
    INPUT_USERNAME_EMPTY("The username input field was empty", false),
    INPUT_USERNAME_TOO_LONG(
            "The username input is longer than the maximum, max: " +
            UserDAOImpl.USERNAME_MAX_LENGTH +
            " characters",
            false
    ),
    INPUT_USERNAME_TOO_SHORT(
            "The username input is shorter than the minimum, min: " +
                    UserDAOImpl.USERNAME_MIN_LENGTH + " characters",
            false
    ),

    /* USER INPUT PASSWORD ERRORS*/
    INPUT_PASSWORD_INCORRECT("Incorrect password", false),
    INPUT_PASSWORD_EMPTY("The password input field was empty", false),
    INPUT_PASSWORD_TOO_LONG(
            "The password input is longer than the maximum, max: " +
            UserDAOImpl.PASSWORD_MAX_LENGTH +
            " characters",
            false
    ),
    INPUT_PASSWORD_TOO_SHORT(
            "The password input is shorter than the minimum, min: " +
            UserDAOImpl.PASSWORD_MIN_LENGTH +
            " characters",
            false
    ),
    INPUT_PASSWORD_CONTAINS_INVALID_CHARACTER(
            "The password input contains an invalid character. The characters: " +
                    "" + //TODO:: Add the illegal characters
                    " can not be used is the password",
            false
    ),
    INPUT_PASSWORD_MISMATCH("The input passwords do not match", false),


    /* ACCOUNT LOGIN/LOGOUT ERRORS */
    LOGIN_NO_CORRESPONDING_ACCOUNT("There is no account corresponding to the given inputs", false),
    NOT_LOGGED_IN("Not currently logged in", false),


    /* ACCOUNT DELETION ERRORS*/
    DELETION_FAILED("Failed to delete account", false),


    INVALID_EMAIL("Invalid Email address", false),
    INVALID_USERNAME("Invalid Username", false);


    private String mErrorMessage;
    private boolean mHideFromUser;

    AccountErrorType(String errorMessage, boolean hideFromUser) {
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