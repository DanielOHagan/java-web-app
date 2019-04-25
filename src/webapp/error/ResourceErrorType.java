package webapp.error;

public enum  ResourceErrorType implements IErrorType {

    /* Resources specific errors */
    NOT_FOUND("The requested resource can not be found or does not exist.");

    String mErrorMessage;

    ResourceErrorType(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return mErrorMessage;
    }
}