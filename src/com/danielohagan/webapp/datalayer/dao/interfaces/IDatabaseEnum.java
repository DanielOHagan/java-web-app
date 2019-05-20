package com.danielohagan.webapp.datalayer.dao.interfaces;

public interface IDatabaseEnum<T> {

    //Return the value of the corresponding Database enum
    String getDatabaseEnumStringValue();
    int getDatabaseEnumColumnValue();

}