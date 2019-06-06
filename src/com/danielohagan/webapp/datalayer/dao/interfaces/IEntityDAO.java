package com.danielohagan.webapp.datalayer.dao.interfaces;

import com.danielohagan.webapp.businesslayer.entities.IEntity;

//Extended by DAO Classes that have a corresponding Entity Class
public interface IEntityDAO<T extends IEntity> {

    T getById(int id);
    boolean exists(int id);

}