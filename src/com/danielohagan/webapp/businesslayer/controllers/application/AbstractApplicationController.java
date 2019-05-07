package com.danielohagan.webapp.businesslayer.controllers.application;

import java.util.Map;

public abstract class AbstractApplicationController {

    protected Map<String, Class> mCommandMap;

    public abstract void processGet();
    public abstract void processPost();

    //Get data from the URL if needed
    protected abstract void processURL();
}