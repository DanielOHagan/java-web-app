package com.danielohagan.webapp.datalayer.dao.interfaces;

import com.danielohagan.webapp.businesslayer.entities.account.User;

public interface IUserDAO extends IEntityDAO<User> {

    User getByEmailAndPassword(String email, String password);

    void updateEmail(User user, String email);
    void updatePassword(User user, String password);
    void updateUsername(User user, String username);

}