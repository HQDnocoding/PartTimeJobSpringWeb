package com.myweb.repositories.impl;

import com.myweb.repositories.ReportRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public class ReportRepositoryImplement implements ReportRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    @Transactional
    public List<Object[]> generateJobReport(Date fromDate, Date toDate) {
        Session session = factory.getObject().getCurrentSession();
        String hql = "SELECT CAST(j.postedDate AS date) AS date, " +
                     "COUNT(DISTINCT j.id) AS jobCount " +
                     "FROM Job j " +
                     "WHERE j.isActive = true " +
                     "AND (:fromDate IS NULL OR j.postedDate >= :fromDate) " +
                     "AND (:toDate IS NULL OR j.postedDate <= :toDate) " +
                     "GROUP BY CAST(j.postedDate AS date)";
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        return query.getResultList();
    }

    @Override
    @Transactional
    public List<Object[]> generateCandidateReport(Date fromDate, Date toDate) {
        Session session = factory.getObject().getCurrentSession();
        String hql = "SELECT CAST(u.registerDate AS date) AS date, " +
                     "COUNT(DISTINCT c.id) AS candidateCount " +
                     "FROM Candidate c JOIN c.userId u " +
                     "WHERE u.isActive = true " +
                     "AND (:fromDate IS NULL OR u.registerDate >= :fromDate) " +
                     "AND (:toDate IS NULL OR u.registerDate <= :toDate) " +
                     "GROUP BY CAST(u.registerDate AS date)";
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        return query.getResultList();
    }

    @Override
    @Transactional
    public List<Object[]> generateCompanyReport(Date fromDate, Date toDate) {
        Session session = factory.getObject().getCurrentSession();
        String hql = "SELECT CAST(u.registerDate AS date) AS date, " +
                     "COUNT(DISTINCT co.id) AS companyCount " +
                     "FROM Company co JOIN co.userId u " +
                     "WHERE u.isActive = true " +
                     "AND (:fromDate IS NULL OR u.registerDate >= :fromDate) " +
                     "AND (:toDate IS NULL OR u.registerDate <= :toDate) " +
                     "GROUP BY CAST(u.registerDate AS date)";
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        return query.getResultList();
    }
}