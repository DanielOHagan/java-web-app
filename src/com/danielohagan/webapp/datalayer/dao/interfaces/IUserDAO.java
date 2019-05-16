package com.danielohagan.webapp.datalayer.dao.interfaces;

import com.danielohagan.webapp.businesslayer.entities.account.User;

public interface IUserDAO extends IEntityDAO<User> {

    User getByEmailAndPassword(String email, String password);

    void updatePassword(int id, String password);
    void createNewUser(User user, String password);
    void deleteUser(int id);

}