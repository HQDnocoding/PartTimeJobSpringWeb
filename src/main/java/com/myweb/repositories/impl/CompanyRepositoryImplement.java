/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.repositories.impl;

import com.myweb.pojo.Company;
import com.myweb.pojo.Job;
import com.myweb.pojo.User;
import com.myweb.repositories.CompanyRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author dat
 */
@Repository
@Transactional
public class CompanyRepositoryImplement implements CompanyRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Company addCompany(Company c) {
        Session session = this.factory.getObject().getCurrentSession();
        try {
            session.persist(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public List<Object[]> getListCompany(Map<String, String> params) {
       Session s=this.factory.getObject().getCurrentSession();
       CriteriaBuilder cb=s.getCriteriaBuilder();
        CriteriaQuery<Company> cq= cb.createQuery(Company.class);
        Root root=cq.from(Company.class);
        cq.select(root);
        
        Query query = s.createQuery(cq);
        
        return query.getResultList();   
 
    }
}
