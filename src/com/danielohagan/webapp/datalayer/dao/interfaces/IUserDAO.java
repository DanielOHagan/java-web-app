package com.danielohagan.webapp.datalayer.dao.interfaces;

import com.danielohagan.webapp.businesslayer.entities.account.User;
import com.danielohagan.webapp.datalayer.dao.databaseenums.UserStatus;

public interface IUserDAO extends IEntityDAO<User> {

    User getByEmailAndPassword(String email, String password);
    User getUserByEmail(String email);

    void updatePassword(int id, String password);
    void updateUserStatus(int id, UserStatus userStatus);

    void createNewUser(User user, String password);
    void deleteUser(int id);

    boolean exists(int id);

    boolean isCorrectPassword(int id, String password);
}