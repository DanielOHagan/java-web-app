package com.danielohagan.webapp.datalayer.dao.interfaces;

import com.danielohagan.webapp.businesslayer.entities.account.User;

public interface IUserDAO extends IEntityDAO<User> {

    User getByEmailAndPassword(String email, String password);

    void updateEmail(int id, String newEmail);
    void updatePassword(int id, String newPassword);
    void updateUsername(int id, String newUsername);
    void updateUser(int id, User user);

    void createNewUser(User user, String password);

    void deleteUser(int id);

}