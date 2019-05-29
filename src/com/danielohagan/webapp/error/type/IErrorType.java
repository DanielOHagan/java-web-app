package com.danielohagan.webapp.error.type;

import com.danielohagan.webapp.error.ErrorSeverity;

public interface IErrorType {

    String getErrorMessage();
    boolean shouldHideFromUser();
    ErrorSeverity getErrorSeverity();

}