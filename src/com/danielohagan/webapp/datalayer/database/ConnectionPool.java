package com.danielohagan.webapp.datalayer.database;

import java.util.List;

public class ConnectionPool {

    List<AbstractConnection> mConnectionList;
    List<AbstractConnection> mUsedConnectionList;

}