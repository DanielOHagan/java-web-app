package com.danielohagan.webapp.datalayer.dao.databaseenums;

public enum ChatPermissionLevel implements IDatabaseEnum {

    //TODO:: Enforce permissions

    NULL("NULL", 0),

    /* Observer can access the Chat Session and view messages but can not change anything */
    OBSERVER("OBSERVER", 1),

    /* Member can send messages but not change the Chat Session's settings */
    MEMBER("MEMBER", 2),

    /* Admin can change the settings of the Chat Session, add and remove Members and alter a User's permissions */
    ADMIN("ADMIN", 3),

    /* Creator can do anything to the Chat Session, including deleting it */
    CREATOR("CREATOR", 4);

    private final String mDatabaseEnumStringValue;
    private final int mDatabaseEnumColumnValue;

    ChatPermissionLevel(String databaseEnumValue, int databaseEnumColumnValue) {
        mDatabaseEnumStringValue = databaseEnumValue;
        mDatabaseEnumColumnValue = databaseEnumColumnValue;
    }

    @Override
    public int asIntValue() {
        return mDatabaseEnumColumnValue;
    }

    @Override
    public String toString() {
        return mDatabaseEnumStringValue;
    }
}