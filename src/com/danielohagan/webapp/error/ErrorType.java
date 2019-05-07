package com.danielohagan.webapp.error;

public enum ErrorType implements IErrorType {

    NO_ERROR("No error has occurred", false),
    UNKNOWN_ERROR("Unknown error has occurred", false),

    /* HTTP Errors */
    HTTP_RESPONSE_CODE_400("Bad Request", false),
    HTTP_RESPONSE_CODE_401("Unauthorised", false),
    HTTP_RESPONSE_CODE_403("Forbidden", false),
    HTTP_RESPONSE_CODE_404("Not Found", false),
    HTTP_RESPONSE_CODE_405("Method Not Allowed", false),
    HTTP_RESPONSE_CODE_406("Not Acceptable", false),
    HTTP_RESPONSE_CODE_407("Proxy Authentication Required", false),
    HTTP_RESPONSE_CODE_408("Request Timeout", false),
    HTTP_RESPONSE_CODE_409("Conflict", false),
    HTTP_RESPONSE_CODE_410("Gone", false),
    HTTP_RESPONSE_CODE_411("Length Required", false),
    HTTP_RESPONSE_CODE_412("Precondition Failed", false),
    HTTP_RESPONSE_CODE_413("Payload Too Large", false),
    HTTP_RESPONSE_CODE_414("URI Too Long", false),
    HTTP_RESPONSE_CODE_415("Unsupported Media Type", false),
    HTTP_RESPONSE_CODE_416("Range Not Satisfiable", false),
    HTTP_RESPONSE_CODE_417("Expectation Failed", false),
    HTTP_RESPONSE_CODE_418("I'm a teapot", false),
    HTTP_RESPONSE_CODE_422("Unprocessable Entity", false),
    HTTP_RESPONSE_CODE_425("Too Early", false),
    HTTP_RESPONSE_CODE_426("Upgrade Required", false),
    HTTP_RESPONSE_CODE_428("Precondition Required", false),
    HTTP_RESPONSE_CODE_429("Too Many Requests", false),
    HTTP_RESPONSE_CODE_431("Request Header Fields Too Large", false),
    HTTP_RESPONSE_CODE_451("Unavailable For Legal Reasons", false),

    HTTP_RESPONSE_CODE_500("Internal Server Error", false),
    HTTP_RESPONSE_CODE_501("Not Implemented", false),
    HTTP_RESPONSE_CODE_502("Bad Gateway", false),
    HTTP_RESPONSE_CODE_503("Service Unavailable", false),
    HTTP_RESPONSE_CODE_504("Gateway Timeout", false),
    HTTP_RESPONSE_CODE_505("HTTP Version Not Supported", false),
    HTTP_RESPONSE_CODE_511("Network Authentication Required", false);

    private String mErrorMessage;
    private boolean mHideFromUser;

    ErrorType(String errorMessage, boolean hideFromUser) {
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