package com.danielohagan.webapp.error;

public interface IErrorType {

    String getErrorMessage();
    boolean shouldHideFromUser();

}