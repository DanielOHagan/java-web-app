package com.danielohagan.webapp.businesslayer.controllers.application;

import com.danielohagan.webapp.businesslayer.AbstractRequestManager;

public abstract class AbstractApplicationController extends AbstractRequestManager {

    public abstract void processGet();
    public abstract void processPost();

    //Get data from the URL if needed
    protected abstract void processURL();

}