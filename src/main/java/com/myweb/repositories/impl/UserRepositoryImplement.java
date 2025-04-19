/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.repositories.impl;

import com.myweb.pojo.User;
import com.myweb.repositories.UserRepository;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author huaquangdat
 */
@Repository
@Transactional
public class UserRepositoryImplement implements UserRepository{

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public User addUser(User user) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(user);
        s.refresh(user);
        
        return user;
    }

    @Override
    public User getUserByUserName(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query query = s.createNamedQuery("User.findByUsername", User.class);
        query.setParameter("username", username);

        return (User) query.getSingleResult();
    }
    
}
