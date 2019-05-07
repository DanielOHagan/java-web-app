package com.danielohagan.webapp.datalayer.dao.interfaces;

import com.danielohagan.webapp.businesslayer.entities.IEntity;

public interface IEntityDAO<T extends IEntity> {

    T getById(int id);

    void store(T entity);
    void update(T entity);
    void delete(T entity);

}