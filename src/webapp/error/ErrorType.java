package webapp.error;

public enum ErrorType implements IErrorType {

    NO_ERROR("No error has occurred"),
    UNKNOWN_ERROR("Unknown error has occurred"),

    /* HTTP Errors */
    HTTP_RESPONSE_CODE_400("Bad Request"),
    HTTP_RESPONSE_CODE_401("Unauthorised"),
    HTTP_RESPONSE_CODE_403("Forbidden"),
    HTTP_RESPONSE_CODE_404("Not Found"),
    HTTP_RESPONSE_CODE_405("Method Not Allowed"),
    HTTP_RESPONSE_CODE_406("Not Acceptable"),
    HTTP_RESPONSE_CODE_407("Proxy Authentication Required"),
    HTTP_RESPONSE_CODE_408("Request Timeout"),
    HTTP_RESPONSE_CODE_409("Conflict"),
    HTTP_RESPONSE_CODE_410("Gone"),
    HTTP_RESPONSE_CODE_411("Length Required"),
    HTTP_RESPONSE_CODE_412("Precondition Failed"),
    HTTP_RESPONSE_CODE_413("Payload Too Large"),
    HTTP_RESPONSE_CODE_414("URI Too Long"),
    HTTP_RESPONSE_CODE_415("Unsupported Media Type"),
    HTTP_RESPONSE_CODE_416("Range Not Satisfiable"),
    HTTP_RESPONSE_CODE_417("Expectation Failed"),
    HTTP_RESPONSE_CODE_418("I'm a teapot"),
    HTTP_RESPONSE_CODE_422("Unprocessable Entity"),
    HTTP_RESPONSE_CODE_425("Too Early"),
    HTTP_RESPONSE_CODE_426("Upgrade Required"),
    HTTP_RESPONSE_CODE_428("Precondition Required"),
    HTTP_RESPONSE_CODE_429("Too Many Requests"),
    HTTP_RESPONSE_CODE_431("Request Header Fields Too Large"),
    HTTP_RESPONSE_CODE_451("Unavailable For Legal Reasons"),

    HTTP_RESPONSE_CODE_500("Internal Server Error"),
    HTTP_RESPONSE_CODE_501("Not Implemented"),
    HTTP_RESPONSE_CODE_502("Bad Gateway"),
    HTTP_RESPONSE_CODE_503("Service Unavailable"),
    HTTP_RESPONSE_CODE_504("Gateway Timeout"),
    HTTP_RESPONSE_CODE_505("HTTP Version Not Supported"),
    HTTP_RESPONSE_CODE_511("Network Authentication Required");

    private String mErrorMessage;

    ErrorType(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    @Override
    public String getErrorMessage() {
        return mErrorMessage;
    }
}