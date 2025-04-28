/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.repositories.impl;

import com.myweb.pojo.Application;
import com.myweb.repositories.ApplicationRepository;
import com.myweb.utils.GeneralUtils;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author huaquangdat
 */
@Repository
@Transactional
public class ApplicationRepositoryImplement implements ApplicationRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Application addOrUpdateApplication(Application a) {
        Session session = this.factory.getObject().getCurrentSession();
        try {
            if (a.getId() == null) {
                session.persist(a);
            } else {
                session.merge(a);
            }
            return a;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Hoặc ném ngoại lệ tùy yêu cầu
        }
    }

    @Override
    public Map<String, Object> getListApplication(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();

        // Tạo CriteriaQuery cho Application
        CriteriaQuery<Application> cq = cb.createQuery(Application.class);
        Root<Application> applicationRoot = cq.from(Application.class);

        List<Predicate> predicates = new ArrayList<>();

        // Tìm kiếm
        if (params != null) {
            // Trạng thái
            String status = params.get("status");
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(applicationRoot.get("status"), status));
            }

            // Tên ứng viên
            String candidateName = params.get("candidateName");
            if (candidateName != null && !candidateName.isEmpty()) {
                predicates.add(cb.like(applicationRoot.get("candidateId").get("fullName"), String.format("%%%s%%", candidateName)));
            }

            // Tên công việc
            String jobName = params.get("jobName");
            if (jobName != null && !jobName.isEmpty()) {
                predicates.add(cb.like(applicationRoot.get("jobId").get("jobName"), String.format("%%%s%%", jobName)));
            }
        }

        // Áp dụng điều kiện
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(Predicate[]::new)));
        }

        // Tạo truy vấn chính
        Query query = s.createQuery(cq);
        // Đếm tổng số bản ghi

        int totalRecords = query.getResultList().size();

        // Phân trang
        int page = 1;
        try {
            page = Integer.parseInt(params.getOrDefault("page", "1"));
        } catch (NumberFormatException e) {
            System.out.println("Invalid page number, defaulting to 1");
        }
        int start = (page - 1) * GeneralUtils.PAGE_SIZE;

       
        query.setFirstResult(start);
        query.setMaxResults(GeneralUtils.PAGE_SIZE);

        // Lấy danh sách kết quả
        List<Application> results = query.getResultList();
        System.out.println("Results: " + results);

        // Tính tổng số trang
        int totalPages = (int) Math.ceil((double) totalRecords / GeneralUtils.PAGE_SIZE);

        // Tạo kết quả trả về
        Map<String, Object> result = new HashMap<>();
        result.put("applications", results);
        result.put("currentPage", page);
        result.put("pageSize", GeneralUtils.PAGE_SIZE);
        result.put("totalPages", totalPages);
        result.put("totalItems", totalRecords);

        return result;
    }

    @Override
    public Application getApplicationById(int applicationId) {
        Session s = this.factory.getObject().getCurrentSession();
        Application application = s.get(Application.class, applicationId);
        return application;
    }
}
