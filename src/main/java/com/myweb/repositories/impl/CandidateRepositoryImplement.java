/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.repositories.impl;

import com.myweb.pojo.Candidate;
import com.myweb.pojo.User;
import com.myweb.repositories.CandidateRepository;
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
public class CandidateRepositoryImplement implements CandidateRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Candidate addOrUpdateCandidate(Candidate c) {
        Session session = this.factory.getObject().getCurrentSession();
        try {
            if (c.getId() == null) {
                session.persist(c);
            } else {
                session.merge(c);
            }
            return c;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Hoặc ném ngoại lệ tùy yêu cầu
        }
    }

    @Override
    public Map<String, Object> getListCandidate(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();

        // Tạo CriteriaQuery cho Candidate
        CriteriaQuery<Candidate> cq = cb.createQuery(Candidate.class);
        Root<Candidate> candidateRoot = cq.from(Candidate.class);

        List<Predicate> predicates = new ArrayList<>();

        // Tìm kiếm
        if (params != null) {
            // Họ tên (tìm kiếm gần đúng)
            String fullName = params.get("fullName");
            if (fullName != null && !fullName.isEmpty()) {
                predicates.add(cb.like(candidateRoot.get("fullName"), String.format("%%%s%%", fullName)));
            }

            // Email (tìm kiếm chính xác)
            String email = params.get("email");
            if (email != null && !email.isEmpty()) {
                predicates.add(cb.equal(candidateRoot.get("email"), email));
            }

            // Thành phố
            String city = params.get("city");
            if (city != null && !city.isEmpty()) {
                predicates.add(cb.equal(candidateRoot.get("city"), city));
            }

            // Số điện thoại
            String phone = params.get("phone");
            if (phone != null && !phone.isEmpty()) {
                predicates.add(cb.equal(candidateRoot.get("phone"), phone));
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
        List<Candidate> results = query.getResultList();
        System.out.println("Results: " + results);

        // Tính tổng số trang
        int totalPages = (int) Math.ceil((double) totalRecords / GeneralUtils.PAGE_SIZE);

        // Tạo kết quả trả về
        Map<String, Object> result = new HashMap<>();
        result.put("candidates", results);
        result.put("currentPage", page);
        result.put("pageSize", GeneralUtils.PAGE_SIZE);
        result.put("totalPages", totalPages);
        result.put("totalItems", totalRecords);

        return result;
    }

    @Override
    public Candidate getCandidateById(int candidateId) {
        Session s = this.factory.getObject().getCurrentSession();
        Candidate candidate = s.get(Candidate.class, candidateId);
        if (candidate != null) {
            System.out.println(candidate.getApplicationCollection());
        }
        return candidate;
    }

    @Override
    public Candidate createCandidate(User u, Candidate c) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            s.persist(u);

            s.persist(c);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi: " + e.getMessage());
        }
        return c;
    }

    @Override
    public Candidate getCandidateByEmail(String email) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Candidate.findByEmail", Candidate.class);
        q.setParameter("email", email);
        List<Candidate> rs = q.getResultList();
        return rs.isEmpty() ? null : rs.get(0);
    }

    @Override
    public Candidate getCandidateByPhone(String phone) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Candidate.findByPhone", Candidate.class);
        q.setParameter("phone", phone);
        List<Candidate> rs = q.getResultList();
        return rs.isEmpty() ? null : rs.get(0);
    }

    @Override
    public List<Candidate> getCandidateList() {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Candidate> cq = cb.createQuery(Candidate.class);
        Root<Candidate> root = cq.from(Candidate.class);
        Predicate isActivePredicate = cb.equal(root.get("userId").get("isActive"), true);
        cq.where(isActivePredicate);

        return s.createQuery(cq).getResultList();
    }

    @Override
    public void deleteCandidate(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Candidate c = this.getCandidateById(id);
        
        s.remove(c.getUserId());
    }

}
