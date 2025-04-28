/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.repositories.impl;

import com.myweb.pojo.Major;
import com.myweb.repositories.MajorRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class MajorRepositoryImplement implements MajorRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Major> getMajors() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Major", Major.class).getResultList();
    }
}
