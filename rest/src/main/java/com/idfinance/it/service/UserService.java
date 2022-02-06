package com.idfinance.it.service;

import com.idfinance.it.pojo.User;

import java.io.Serializable;
import java.util.List;

public interface UserService {
    Serializable saveUser(User user);

    List<User> readAllUsers();
}
