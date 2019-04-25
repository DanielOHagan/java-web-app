package webapp.error;

import webapp.datalayer.account.DatabaseUser;

public enum AccountErrorType implements IErrorType {

    /* ACCOUNT ERRORS */


    DOES_NOT_EXIST("The requested account does not exist"),
    DOES_NOT_HAVE_PERMISSION("Your account does not have the required permission to perform the task"),

    FAILED_TO_RETRIEVE_EMAIL("Failed to retrieve account Email"),
    FAILED_TO_RETRIEVE_USERNAME("Failed to retrieve account Username"),


    /* ACCOUNT CREATION ERRORS */
    /* ACCOUNT CREATION EMAIL ERRORS */
    CREATION_EMAIL_TAKEN("Failed to create account: Email taken"),

    /* ACCOUNT CREATION PASSWORD ERRORS */
    CREATION_USERNAME_TAKEN("Failed to create account: Username taken"),



    /* USER INPUT ERRORS */
    /* USER INPUT EMAIL ERRORS*/
    INPUT_EMAIL_INCORRECT("Incorrect email"),
    INPUT_EMAIL_EMPTY("The email input field was empty"),
    INPUT_EMAIL_TOO_LONG("The email input is longer than the maximum, max: " +
            DatabaseUser.EMAIL_MAX_LENGTH +
            " characters"
    ),
    INPUT_EMAIL_TOO_SHORT("The email input is shorter than the minimum, min: " +
            DatabaseUser.EMAIL_MIN_LENGTH +
            " characters"
    ),

    /* USER INPUT USERNAME ERRORS*/
    INPUT_USERNAME_EMPTY("The username input field was empty"),
    INPUT_USERNAME_TOO_LONG(
            "The username input is longer than the maximum, max: " +
                    DatabaseUser.USERNAME_MAX_LENGTH +
                    " characters"
    ),
    INPUT_USERNAME_TOO_SHORT(
            "The username input is shorter than the minimum, min: " +
                    DatabaseUser.USERNAME_MIN_LENGTH + " characters"
    ),

    /* USER INPUT PASSWORD ERRORS*/
    INPUT_PASSWORD_INCORRECT("Incorrect password"),
    INPUT_PASSWORD_EMPTY("The password input field was empty"),
    INPUT_PASSWORD_TOO_LONG("The password input is longer than the maximum, max: " +
            DatabaseUser.PASSWORD_MAX_LENGTH +
            " characters"
    ),
    INPUT_PASSWORD_TOO_SHORT("The password input is shorter than the minimum, min: " +
            DatabaseUser.PASSWORD_MIN_LENGTH +
            " characters"
    ),



    /* ACCOUNT LOGIN ERRORS */
    LOGIN_NO_CORRESPONDING_ACCOUNT("There is no account corresponding to the given inputs"),

    /* ACCOUNT LOGIN EMAIL ERRORS */


    /* ACCOUNT LOGIN PASSWORD ERRORS */


    INVALID_EMAIL("Invalid Email address"),
    INVALID_USERNAME("Invalid Username");

    private String mErrorMessage;

    AccountErrorType(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return mErrorMessage;
    }
}