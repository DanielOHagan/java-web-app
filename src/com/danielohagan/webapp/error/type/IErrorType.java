package com.danielohagan.webapp.error.type;

public interface IErrorType {

    String getErrorMessage();
    boolean shouldHideFromUser();

}