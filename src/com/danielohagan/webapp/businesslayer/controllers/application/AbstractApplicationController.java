package com.danielohagan.webapp.businesslayer.controllers.application;

import java.util.Map;

public abstract class AbstractApplicationController {

    protected static final String REQUEST_ATTRIBUTE_ERROR_MESSAGE = "errorMessage";
    protected static final String REQUEST_ATTRIBUTE_INFO_MESSAGE = "infoMessage";

    protected Map<String, Class> mCommandMap;

    public abstract void processGet();
    public abstract void processPost();

    //Get data from the URL if needed
    protected abstract void processURL();
}