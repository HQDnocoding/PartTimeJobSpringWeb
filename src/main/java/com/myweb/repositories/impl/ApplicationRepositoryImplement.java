/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.repositories.impl;

import com.myweb.pojo.Application;
import com.myweb.pojo.User;
import com.myweb.repositories.ApplicationRepository;
import com.myweb.utils.GeneralUtils;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author huaquangdat
 */
@Repository
@Transactional
public class ApplicationRepositoryImplement implements ApplicationRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    // Thêm mới hoặc cập nhật một đơn ứng tuyển 
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
            return null;
        }
    }

    // Lấy danh sách đơn ứng tuyển với các tiêu chí lọc và phân trang
    @Override
    public Map<String, Object> getListApplication(Map<String, String> params, User user) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();

        // Tạo CriteriaQuery cho Application
        CriteriaQuery<Application> cq = cb.createQuery(Application.class);
        Root<Application> applicationRoot = cq.from(Application.class);

        List<Predicate> predicates = new ArrayList<>();

        // Tìm kiếm
        if (params != null) {

            if (user.getRole().equals(GeneralUtils.Role.ROLE_COMPANY.toString())) {
                predicates.add(cb.equal(applicationRoot.get("jobId").get("companyId").get("id"), user.getCompany().getId()));
            } else if (Objects.equals(user.getRole(), GeneralUtils.Role.ROLE_CANDIDATE.toString())) {
                predicates.add(cb.equal(applicationRoot.get("candidateId").get("id"), user.getCandidate().getId()));
            }

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

            String jobIdStr = params.get("jobId");
            if (jobIdStr != null && !jobIdStr.isEmpty()) {
                try {
                    Long jobId = Long.valueOf(jobIdStr);
                    predicates.add(cb.equal(applicationRoot.get("jobId").get("id"), jobId));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid jobId: " + jobIdStr);
                }
            }
        }

        // Áp dụng điều kiện
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(Predicate[]::new)));
        }

        String sortBy = params.getOrDefault("sortBy", "appliedDate");
        String sortOrder = params.getOrDefault("sortOrder", "desc");

        //sắp xếp
        switch (sortBy) {
            case "candidateId.name" ->                 {
                    Path<String> sortPath = applicationRoot.get("candidateId").get("name");
                    cq.orderBy(sortOrder.equalsIgnoreCase("asc") ? cb.asc(sortPath) : cb.desc(sortPath));
                }
            case "jobId.jobName" ->                 {
                    Path<String> sortPath = applicationRoot.get("jobId").get("jobName");
                    cq.orderBy(sortOrder.equalsIgnoreCase("asc") ? cb.asc(sortPath) : cb.desc(sortPath));
                }
            default -> cq.orderBy(sortOrder.equalsIgnoreCase("asc")
                        ? cb.asc(applicationRoot.get(sortBy))
                        : cb.desc(applicationRoot.get(sortBy)));
        }

        Query query = s.createQuery(cq);

        int totalRecords = query.getResultList().size();

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

    // Lấy chi tiết một đơn ứng tuyển theo ID
    @Override
    public Application getApplicationById(int applicationId) {
        System.out.println(applicationId);
        Session s = this.factory.getObject().getCurrentSession();
        Application application = s.get(Application.class, applicationId);
        System.out.println(application);
        return application;
    }

    // Xóa một đơn ứng tuyển theo ID
    @Override
    public void deleteApplication(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Application a = getApplicationById(id);
        s.remove(a);
    }

}
