/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.myweb.repositories.impl;

import com.myweb.dto.JobDTO;
import com.myweb.pojo.Company;
import com.myweb.pojo.Job;
import com.myweb.pojo.Major;
import com.myweb.pojo.Day;
import com.myweb.repositories.JobRepository;
import com.myweb.utils.GeneralUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Join;
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
import java.util.stream.Collectors;

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
    @SuppressWarnings("unchecked")
    public Map<String, Object> searchJobs(Map<String, String> params) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Job> jobRoot = cq.from(Job.class);
        Join<Job, Company> companyJoin = jobRoot.join("companyId");
        Join<Job, Major> majorJoin = jobRoot.join("marjorJobCollection").join("majorId");
        Join<Job, Day> dayJoin = jobRoot.join("dayJobCollection").join("dayId");

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
                    predicates.add(cb.equal(majorJoin.get("id"), Integer.parseInt(majorId)));
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
                    predicates.add(cb.equal(dayJoin.get("id"), Integer.parseInt(dayId)));
                } catch (NumberFormatException e) {
                    logger.warning("Invalid dayId format: " + dayId);
                }
            }
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(Predicate[]::new)));
        }

        cq.multiselect(
            jobRoot.get("id"),
            jobRoot.get("jobName"),
            jobRoot.get("description"),
            jobRoot.get("jobRequired"),
            jobRoot.get("salaryMin"),
            jobRoot.get("salaryMax"),
            jobRoot.get("fullAddress"),
            jobRoot.get("city"),
            jobRoot.get("district"),
            jobRoot.get("status"),
            jobRoot.get("isActive"),
            jobRoot.get("postedDate"),
            companyJoin.get("name"),
            majorJoin.get("name"),
            dayJoin.get("name")
        );

        CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
        countCq.select(cb.count(countCq.from(Job.class)));
        countCq.where(cb.and(predicates.toArray(Predicate[]::new)));
        Long totalRecords = session.createQuery(countCq).getSingleResult();

        jakarta.persistence.Query query = session.createQuery(cq);
        int page = 1;
        if (params != null) {
            try {
                page = Integer.parseInt(params.getOrDefault("page", GeneralUtils.PAGE));
            } catch (NumberFormatException e) {
                logger.warning("Invalid page format: " + params.get("page"));
            }
        }
        int start = (page - 1) * GeneralUtils.PAGE_SIZE;
        query.setFirstResult(start);
        query.setMaxResults(GeneralUtils.PAGE_SIZE);

        List<Object[]> rawResults = query.getResultList();
        List<JobDTO> results = mapToJobDTOList(rawResults);

        int totalPages = (int) Math.ceil((double) totalRecords / GeneralUtils.PAGE_SIZE);

        Map<String, Object> result = new HashMap<>();
        result.put("jobs", results);
        result.put("currentPage", page);
        result.put("pageSize", GeneralUtils.PAGE_SIZE);
        result.put("totalPages", totalPages);
        result.put("totalItems", totalRecords);

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<JobDTO> getListJobByMajor(int majorID) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Job> jobRoot = cq.from(Job.class);
        Join<Job, Company> companyJoin = jobRoot.join("companyId");
        Join<Job, Major> majorJoin = jobRoot.join("marjorJobCollection").join("majorId");
        Join<Job, Day> dayJoin = jobRoot.join("dayJobCollection").join("dayId");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(jobRoot.get("isActive"), true));
        predicates.add(cb.equal(jobRoot.get("status"), GeneralUtils.Status.approved.toString()));
        predicates.add(cb.equal(majorJoin.get("id"), majorID));

        cq.where(cb.and(predicates.toArray(Predicate[]::new)));

        cq.multiselect(
            jobRoot.get("id"),
            jobRoot.get("jobName"),
            jobRoot.get("description"),
            jobRoot.get("jobRequired"),
            jobRoot.get("salaryMin"),
            jobRoot.get("salaryMax"),
            jobRoot.get("fullAddress"),
            jobRoot.get("city"),
            jobRoot.get("district"),
            jobRoot.get("status"),
            jobRoot.get("isActive"),
            jobRoot.get("postedDate"),
            companyJoin.get("name"),
            majorJoin.get("name"),
            dayJoin.get("name")
        );

        List<Object[]> rawResults = session.createQuery(cq).getResultList();
        return mapToJobDTOList(rawResults);
    }

    private List<JobDTO> mapToJobDTOList(List<Object[]> rawResults) {
        return rawResults.stream().map(row -> new JobDTO(
            (Integer) row[0],           // id
            (String) row[1],            // jobName
            (String) row[2],            // description
            (String) row[3],            // jobRequired
            (BigInteger) row[4],        // salaryMin
            (BigInteger) row[5],        // salaryMax
            (String) row[6],            // fullAddress
            (String) row[7],            // city
            (String) row[8],            // district
            (String) row[9],            // status
            (Boolean) row[10],          // isActive
            (java.util.Date) row[11],   // postedDate
            (String) row[12],           // companyName
            (String) row[13],           // majorName
            (String) row[14]            // dayName
        )).collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public JobDTO getDetailJobById(int jobID) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Job> jobRoot = cq.from(Job.class);
        Join<Job, Company> companyJoin = jobRoot.join("companyId");
        Join<Job, Major> majorJoin = jobRoot.join("marjorJobCollection").join("majorId");
        Join<Job, Day> dayJoin = jobRoot.join("dayJobCollection").join("dayId");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(jobRoot.get("id"), jobID));
        predicates.add(cb.equal(jobRoot.get("isActive"), true));
        predicates.add(cb.equal(jobRoot.get("status"), GeneralUtils.Status.approved.toString()));

        cq.where(cb.and(predicates.toArray(Predicate[]::new)));

        cq.multiselect(
            jobRoot.get("id"),
            jobRoot.get("jobName"),
            jobRoot.get("description"),
            jobRoot.get("jobRequired"),
            jobRoot.get("salaryMin"),
            jobRoot.get("salaryMax"),
            jobRoot.get("fullAddress"),
            jobRoot.get("city"),
            jobRoot.get("district"),
            jobRoot.get("status"),
            jobRoot.get("isActive"),
            jobRoot.get("postedDate"),
            companyJoin.get("name"),
            majorJoin.get("name"),
            dayJoin.get("name")
        );

        List<Object[]> rawResults = session.createQuery(cq).getResultList();
        List<JobDTO> results = mapToJobDTOList(rawResults);

        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<JobDTO> getListJobByRecommend(int majorID, int cityID) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Job> jobRoot = cq.from(Job.class);
        Join<Job, Company> companyJoin = jobRoot.join("companyId");
        Join<Job, Major> majorJoin = jobRoot.join("marjorJobCollection").join("majorId");
        Join<Job, Day> dayJoin = jobRoot.join("dayJobCollection").join("dayId");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(jobRoot.get("isActive"), true));
        predicates.add(cb.equal(jobRoot.get("status"), GeneralUtils.Status.approved.toString()));
        predicates.add(cb.equal(majorJoin.get("id"), majorID));
        predicates.add(cb.equal(jobRoot.get("city"), String.valueOf(cityID)));

        cq.where(cb.and(predicates.toArray(Predicate[]::new)));

        cq.multiselect(
            jobRoot.get("id"),
            jobRoot.get("jobName"),
            jobRoot.get("description"),
            jobRoot.get("jobRequired"),
            jobRoot.get("salaryMin"),
            jobRoot.get("salaryMax"),
            jobRoot.get("fullAddress"),
            jobRoot.get("city"),
            jobRoot.get("district"),
            jobRoot.get("status"),
            jobRoot.get("isActive"),
            jobRoot.get("postedDate"),
            companyJoin.get("name"),
            majorJoin.get("name"),
            dayJoin.get("name")
        );

        List<Object[]> rawResults = session.createQuery(cq).getResultList();
        return mapToJobDTOList(rawResults);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Job> getListJobByCompanyId(int companyID) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Job> cq = cb.createQuery(Job.class);
        Root<Job> jobRoot = cq.from(Job.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(jobRoot.get("companyId").get("id"), companyID));
        predicates.add(cb.equal(jobRoot.get("isActive"), true));
        predicates.add(cb.equal(jobRoot.get("status"), GeneralUtils.Status.approved.toString()));

        cq.where(cb.and(predicates.toArray(Predicate[]::new)));
        cq.select(jobRoot);

        return session.createQuery(cq).getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<JobDTO> getListJobByCompanyId1(int companyID) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Job> jobRoot = cq.from(Job.class);
        Join<Job, Company> companyJoin = jobRoot.join("companyId");
        Join<Job, Major> majorJoin = jobRoot.join("marjorJobCollection").join("majorId");
        Join<Job, Day> dayJoin = jobRoot.join("dayJobCollection").join("dayId");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(jobRoot.get("companyId").get("id"), companyID));
        predicates.add(cb.equal(jobRoot.get("isActive"), true));
        predicates.add(cb.equal(jobRoot.get("status"), GeneralUtils.Status.approved.toString()));

        cq.where(cb.and(predicates.toArray(Predicate[]::new)));

        cq.multiselect(
            jobRoot.get("id"),
            jobRoot.get("jobName"),
            jobRoot.get("description"),
            jobRoot.get("jobRequired"),
            jobRoot.get("salaryMin"),
            jobRoot.get("salaryMax"),
            jobRoot.get("fullAddress"),
            jobRoot.get("city"),
            jobRoot.get("district"),
            jobRoot.get("status"),
            jobRoot.get("isActive"),
            jobRoot.get("postedDate"),
            companyJoin.get("name"),
            majorJoin.get("name"),
            dayJoin.get("name")
        );

        List<Object[]> rawResults = session.createQuery(cq).getResultList();
        return mapToJobDTOList(rawResults);
    }

    @Override
    public List<JobDTO> getListJobByCompanyExceptCurrentJob(int companyID, int jobID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Job getNameJob(int jobID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<JobDTO> getListJobByCandidate(int candidateID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<JobDTO> getListJobByCheckAdmin() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<JobDTO> getListJobByMajorAndCity(int majorID, String city, String kw) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Job> getListJobForManageCompany(int companyID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateJob(int jobID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<JobDTO> getListJobByCityKw(String city, String kw) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<JobDTO> getListJobByCityKwPage(String city, String kw, int page) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long countJob() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteJob(int jobID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<JobDTO> getListJobForManage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addJob(Job j) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}