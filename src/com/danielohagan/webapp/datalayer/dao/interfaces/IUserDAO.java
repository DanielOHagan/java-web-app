package com.danielohagan.webapp.datalayer.dao.interfaces;

import com.danielohagan.webapp.businesslayer.entities.account.User;
import com.danielohagan.webapp.datalayer.dao.databaseenums.UserStatus;

import java.util.List;
import java.util.Map;

public interface IUserDAO extends IEntityDAO<User> {

    User getByEmailAndPassword(String email, String password);
    User getUserByEmail(String email);
    List<User> getByUsername(String username);

    void updatePassword(int id, String password);
    void updateUserStatus(int id, UserStatus userStatus);

    void createNewUser(User user, String password);
    void deleteUser(int id);

    boolean isCorrectPassword(int id, String password);

    Map<String, String> getColumnStringsById(int id,  String... columnNames);
    Map<String, Integer> getColumnIntegersById(int id, String... columnNames);

    String getColumnString(int id, String columnName);
    Integer getColumnInteger(int id, String columnName);

}