package webapp.error;

public enum  DatabaseErrorType implements IErrorType {

    /* Database Errors */
    DATABASE_CONNECTION_FAILED("Could not connect to DataBase"),
    FAILED_TO_RETRIEVE_TABLE("Table can not be found");

    private String mErrorMessage;

    DatabaseErrorType(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return mErrorMessage;
    }
}