package com.idfinance.it.service;

import com.idfinance.it.dao.UserDao;
import com.idfinance.it.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@Transactional(propagation = Propagation.NESTED)
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public Serializable saveUser(User user) {
        return this.userDao.saveUser(user);
    }

    @Override
    public List<User> readAllUsers() {
        return this.userDao.readAllUsers();
    }
}
