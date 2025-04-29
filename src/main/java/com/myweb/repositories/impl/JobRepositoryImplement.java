/*
 * Click nbfs://.netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://.netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.repositories.impl;

import com.myweb.pojo.Job;
import com.myweb.repositories.JobRepository;
import com.myweb.utils.GeneralUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.JoinType;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Implementation of JobRepository for managing Job entities.
 * Handles database operations for searching and filtering jobs.
 *
 * @author Hau
 */
@Repository
@Transactional
public class JobRepositoryImplement implements JobRepository {

    private static final Logger logger = Logger.getLogger(JobRepositoryImplement.class.getName());

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Map<String, Object> searchJobs(Map<String, String> params) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        // Đếm tổng số bản ghi
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Job> countRoot = countQuery.from(Job.class);
        List<Predicate> countPredicates = buildPredicates(params, cb, countRoot);
        countQuery.select(cb.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));
        Long totalRecords = session.createQuery(countQuery).getSingleResult();

        // Truy vấn danh sách công việc với phân trang
        CriteriaQuery<Job> cq = cb.createQuery(Job.class);
        Root<Job> jobRoot = cq.from(Job.class);

        // Chỉ fetch companyId, không fetch các collection
        jobRoot.fetch("companyId", JoinType.LEFT);

        List<Predicate> predicates = buildPredicates(params, cb, jobRoot);
        cq.where(predicates.toArray(new Predicate[0]));

        // Phân trang
        int page = 1;
        if (params != null) {
            try {
                page = Integer.parseInt(params.getOrDefault("page", GeneralUtils.PAGE));
            } catch (NumberFormatException e) {
                logger.warning("Invalid page format: " + params.get("page"));
            }
        }
        int start = (page - 1) * GeneralUtils.PAGE_SIZE;
        List<Job> jobs = session.createQuery(cq)
            .setFirstResult(start)
            .setMaxResults(GeneralUtils.PAGE_SIZE)
            .getResultList();

        // Initialize collections within the transaction
        for (Job job : jobs) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }

        // Tính tổng số trang
        int totalPages = (int) Math.ceil((double) totalRecords / GeneralUtils.PAGE_SIZE);

        // Trả về kết quả
        Map<String, Object> result = new HashMap<>();
        result.put("jobs", jobs);
        result.put("currentPage", page);
        result.put("pageSize", GeneralUtils.PAGE_SIZE);
        result.put("totalPages", totalPages);
        result.put("totalItems", totalRecords);

        return result;
    }

    private List<Predicate> buildPredicates(Map<String, String> params, CriteriaBuilder cb, Root<Job> jobRoot) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(jobRoot.get("isActive"), true));
        predicates.add(cb.equal(jobRoot.get("status"), GeneralUtils.Status.approved.toString()));

        if (params != null) {
            String keyword = params.get("keyword");
            if (keyword != null && !keyword.isEmpty()) {
                predicates.add(cb.or(
                    cb.like(cb.lower(jobRoot.get("jobName")), "%" + keyword.toLowerCase() + "%"),
                    cb.like(cb.lower(jobRoot.get("description")), "%" + keyword.toLowerCase() + "%")
                ));
            }

            String majorId = params.get("majorId");
            if (majorId != null && !majorId.isEmpty()) {
                try {
                    predicates.add(cb.equal(jobRoot.join("marjorJobCollection").join("majorId").get("id"), Integer.parseInt(majorId)));
                } catch (NumberFormatException e) {
                    logger.warning("Invalid majorId format: " + majorId);
                }
            }

            String salaryMin = params.get("salaryMin");
            if (salaryMin != null && !salaryMin.isEmpty()) {
                try {
                    predicates.add(cb.greaterThanOrEqualTo(jobRoot.get("salaryMin"), new BigInteger(salaryMin)));
                } catch (NumberFormatException e) {
                    logger.warning("Invalid salaryMin format: " + salaryMin);
                }
            }
            String salaryMax = params.get("salaryMax");
            if (salaryMax != null && !salaryMax.isEmpty()) {
                try {
                    predicates.add(cb.lessThanOrEqualTo(jobRoot.get("salaryMax"), new BigInteger(salaryMax)));
                } catch (NumberFormatException e) {
                    logger.warning("Invalid salaryMax format: " + salaryMax);
                }
            }

            String city = params.get("city");
            if (city != null && !city.isEmpty()) {
                predicates.add(cb.equal(jobRoot.get("city"), city));
            }
            String district = params.get("district");
            if (district != null && !district.isEmpty()) {
                predicates.add(cb.equal(jobRoot.get("district"), district));
            }

            String dayId = params.get("dayId");
            if (dayId != null && !dayId.isEmpty()) {
                try {
                    predicates.add(cb.equal(jobRoot.join("dayJobCollection").join("dayId").get("id"), Integer.parseInt(dayId)));
                } catch (NumberFormatException e) {
                    logger.warning("Invalid dayId format: " + dayId);
                }
            }
        }

        return predicates;
    }

    @Override
    public List<Job> getListJobByMajor(int majorId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Job> cq = cb.createQuery(Job.class);
        Root<Job> jobRoot = cq.from(Job.class);

        jobRoot.fetch("companyId", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(jobRoot.get("isActive"), true));
        predicates.add(cb.equal(jobRoot.get("status"), GeneralUtils.Status.approved.toString()));
        predicates.add(cb.equal(jobRoot.join("marjorJobCollection").join("majorId").get("id"), majorId));

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        List<Job> jobs = session.createQuery(cq).getResultList();

        // Initialize collections within the transaction
        for (Job job : jobs) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }

        return jobs;
    }

    @Override
    public Job getJobById(int jobId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Job> cq = cb.createQuery(Job.class);
        Root<Job> jobRoot = cq.from(Job.class);

        jobRoot.fetch("companyId", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(jobRoot.get("id"), jobId));
        predicates.add(cb.equal(jobRoot.get("isActive"), true));
        predicates.add(cb.equal(jobRoot.get("status"), GeneralUtils.Status.approved.toString()));

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        Job job = session.createQuery(cq).uniqueResult();

        // Initialize collections within the transaction
        if (job != null) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }

        return job;
    }

    @Override
    public List<Job> getListJobByRecommend(int majorId, int cityId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Job> cq = cb.createQuery(Job.class);
        Root<Job> jobRoot = cq.from(Job.class);

        jobRoot.fetch("companyId", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(jobRoot.get("isActive"), true));
        predicates.add(cb.equal(jobRoot.get("status"), GeneralUtils.Status.approved.toString()));
        predicates.add(cb.equal(jobRoot.join("marjorJobCollection").join("majorId").get("id"), majorId));
        predicates.add(cb.equal(jobRoot.get("city"), String.valueOf(cityId)));

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        List<Job> jobs = session.createQuery(cq).getResultList();

        // Initialize collections within the transaction
        for (Job job : jobs) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }

        return jobs;
    }

    @Override
    public List<Job> getListJobByCompanyId(int companyId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Job> cq = cb.createQuery(Job.class);
        Root<Job> jobRoot = cq.from(Job.class);

        jobRoot.fetch("companyId", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(jobRoot.get("companyId").get("id"), companyId));
        predicates.add(cb.equal(jobRoot.get("isActive"), true));
        predicates.add(cb.equal(jobRoot.get("status"), GeneralUtils.Status.approved.toString()));

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        List<Job> jobs = session.createQuery(cq).getResultList();

        // Initialize collections within the transaction
        for (Job job : jobs) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }

        return jobs;
    }

    @Override
    public List<Job> getListJobByCompanyId1(int companyId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Job> cq = cb.createQuery(Job.class);
        Root<Job> jobRoot = cq.from(Job.class);

        jobRoot.fetch("companyId", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(jobRoot.get("companyId").get("id"), companyId));
        predicates.add(cb.equal(jobRoot.get("isActive"), true));
        predicates.add(cb.equal(jobRoot.get("status"), GeneralUtils.Status.approved.toString()));

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        List<Job> jobs = session.createQuery(cq).getResultList();

        // Initialize collections within the transaction
        for (Job job : jobs) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }

        return jobs;
    }

    @Override
    public List<Job> getListJobByCompanyExceptCurrentJob(int companyId, int jobId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Job getNameJob(int jobId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Job> getListJobByCandidate(int candidateId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Job> getListJobByCheckAdmin() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Job> getListJobByMajorAndCity(int majorId, String city, String kw) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Job> getListJobForManageCompany(int companyId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateJob(int jobId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Job> getListJobByCityKw(String city, String kw) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Job> getListJobByCityKwPage(String city, String kw, int page) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long countJob() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteJob(int jobId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Job> getListJobForManage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addJob(Job j) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}