package com.danielohagan.webapp.businesslayer.controllers.application;

public abstract class AbstractApplicationController {

    protected static final String REQUEST_TRUE = "true";
    protected static final String REQUEST_FALSE = "false";

    protected static final String REQUEST_ATTRIBUTE_ERROR_MESSAGE = "errorMessage";
    protected static final String REQUEST_ATTRIBUTE_INFO_MESSAGE = "infoMessage";

    protected static final String REQUEST_ATTRIBUTE_HAS_ERROR = "hasError";
    protected static final String REQUEST_ATTRIBUTE_HAS_INFO = "hasInfo";

    public abstract void processGet();
    public abstract void processPost();

    //Get data from the URL if needed
    protected abstract void processURL();
}