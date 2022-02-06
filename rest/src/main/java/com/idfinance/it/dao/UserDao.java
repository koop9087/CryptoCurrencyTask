package com.idfinance.it.dao;

import com.idfinance.it.pojo.User;

import java.io.Serializable;
import java.util.List;

public interface UserDao {
    Serializable saveUser(User user);

    List<User> readAllUsers();
}
