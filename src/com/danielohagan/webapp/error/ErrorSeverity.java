package com.danielohagan.webapp.error;

public enum ErrorSeverity {

    INFO, //IErrorType that provides information
    MINOR, //IErrorType that prevents a minor process from working but do not stop the page from loading
    MAJOR, //IErrorType that prevents a main process from working but do not stop the page from loading
    FATAL //IErrorType that prevents the page from loading

}