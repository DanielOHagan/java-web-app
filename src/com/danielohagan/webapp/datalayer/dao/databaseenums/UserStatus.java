package com.danielohagan.webapp.datalayer.dao.databaseenums;

import com.danielohagan.webapp.datalayer.dao.interfaces.IDatabaseEnum;

public enum UserStatus implements IDatabaseEnum {

    NULL("NULL", 0),

    NEW("NEW", 1),
    ACTIVE("ACTIVE", 2),

    BANNED("BANNED", 3),
    SUSPENDED("SUSPENDED", 4),
    DELETED("DELETED", 5);

    private final String mDatabaseEnumStringValue;
    private final int mDatabaseEnumColumnValue;

    UserStatus(String databaseEnumValue, int databaseEnumColumnValue) {
        mDatabaseEnumStringValue = databaseEnumValue;
        mDatabaseEnumColumnValue = databaseEnumColumnValue;
    }

    @Override
    public String getDatabaseEnumStringValue() {
        return mDatabaseEnumStringValue;
    }

    @Override
    public int getDatabaseEnumColumnValue() {
        return mDatabaseEnumColumnValue;
    }

    @Override
    public String toString() {
        return mDatabaseEnumStringValue;
    }
}