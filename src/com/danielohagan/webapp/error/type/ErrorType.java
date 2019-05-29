package com.danielohagan.webapp.error.type;

import com.danielohagan.webapp.error.ErrorSeverity;

public enum ErrorType implements IErrorType {

    //NO_ERROR("No error has occurred", false, ErrorSeverity.INFO),
    UNKNOWN_ERROR("Unknown error has occurred", false, ErrorSeverity.INFO),

    /* HTTP Errors */
    HTTP_RESPONSE_CODE_400("Bad Request", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_401("Unauthorised", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_403("Forbidden", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_404("Not Found", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_405("Method Not Allowed", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_406("Not Acceptable", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_407("Proxy Authentication Required", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_408("Request Timeout", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_409("Conflict", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_410("Gone", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_411("Length Required", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_412("Precondition Failed", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_413("Payload Too Large", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_414("URI Too Long", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_415("Unsupported Media Type", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_416("Range Not Satisfiable", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_417("Expectation Failed", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_418("I'm a teapot", false, ErrorSeverity.INFO),
    HTTP_RESPONSE_CODE_422("Unprocessable Entity", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_425("Too Early", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_426("Upgrade Required", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_428("Precondition Required", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_429("Too Many Requests", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_431("Request Header Fields Too Large", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_451("Unavailable For Legal Reasons", false, ErrorSeverity.FATAL),

    HTTP_RESPONSE_CODE_500("Internal Server Error", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_501("Not Implemented", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_502("Bad Gateway", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_503("Service Unavailable", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_504("Gateway Timeout", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_505("HTTP Version Not Supported", false, ErrorSeverity.FATAL),
    HTTP_RESPONSE_CODE_511("Network Authentication Required", false, ErrorSeverity.FATAL);

    private String mErrorMessage;
    private boolean mHideFromUser;
    private ErrorSeverity mErrorSeverity;

    ErrorType(
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