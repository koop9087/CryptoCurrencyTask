package com.idfinance.it.dao;

import com.idfinance.it.pojo.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    public static final String USER_QUERY = "from User";
    @Autowired
    @Qualifier("customSessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public Serializable saveUser(User user) {
        Session session = this.sessionFactory.getCurrentSession();
        return session.save(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> readAllUsers() {
        Session session = this.sessionFactory.getCurrentSession();
        return session.createQuery(USER_QUERY).list();
    }
}
