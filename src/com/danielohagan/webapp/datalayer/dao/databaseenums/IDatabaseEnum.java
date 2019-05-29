package com.danielohagan.webapp.datalayer.dao.databaseenums;

public interface IDatabaseEnum {

    //Return the value of the corresponding Database enum
    String getDatabaseEnumStringValue();
    int getDatabaseEnumColumnValue();

}