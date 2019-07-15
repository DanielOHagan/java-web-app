package com.danielohagan.webapp.datalayer.dao.databaseenums;

public interface IDatabaseEnum {

    //Return the value of the corresponding Database enum
    @Override
    String toString();
    int asIntValue();

}