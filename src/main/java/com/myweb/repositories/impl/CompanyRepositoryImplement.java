/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.repositories.impl;

import com.myweb.dto.CreateCompanyDTO;
import com.myweb.pojo.Company;
import com.myweb.pojo.ImageWorkplace;
import com.myweb.pojo.Job;
import com.myweb.pojo.User;
import com.myweb.repositories.CompanyRepository;
import com.myweb.utils.GeneralUtils;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public Company addOrUpdateCompany(Company c) {
        Session session = this.factory.getObject().getCurrentSession();
        System.out.println(c.getId());

        try {
            if (c.getId() == null) {
                session.persist(c);
            } else {
                session.merge(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public Map<String, Object> getListCompany(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();

        // Tạo CriteriaQuery cho Company
        CriteriaQuery<Company> cq = cb.createQuery(Company.class);
        Root<Company> companyRoot = cq.from(Company.class);

        List<Predicate> predicates = new ArrayList<>();

        // Tìm kiếm
        if (params != null) {
            String name = params.get("name");
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(companyRoot.get("name"), String.format("%%%s%%", name)));
            }
            String taxCode = params.get("taxCode");
            if (taxCode != null && !taxCode.isEmpty()) {
                predicates.add(cb.equal(companyRoot.get("taxCode"), taxCode));
            }
            String status = params.get("status");
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(companyRoot.get("status"), status));
            }
            String city = params.get("city");
            if (city != null && !city.isEmpty()) {
                predicates.add(cb.equal(companyRoot.get("city"), city));
            }
            String district = params.get("district");
            if (district != null && !district.isEmpty()) {
                predicates.add(cb.equal(companyRoot.get("district"), district));
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
        List<Company> results = query.getResultList();
        System.out.println("Results: " + results);

        // Tính tổng số trang
        int totalPages = (int) Math.ceil((double) totalRecords / GeneralUtils.PAGE_SIZE);

        // Tạo kết quả trả về
        Map<String, Object> result = new HashMap<>();
        result.put("companies", results);
        result.put("currentPage", page);
        result.put("pageSize", GeneralUtils.PAGE_SIZE);
        result.put("totalPages", totalPages);
        result.put("totalItems", totalRecords);

        return result;
    }

    @Override
    public Company getCompanyById(int companyId) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Company.class, companyId);
    }

    @Override
    public void deleteCompany(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Company c = this.getCompanyById(id);

        s.remove(c);
    }

    @Override
    public Company createCompanyDTO(User u, Company c) {
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
    public Company getCompanyByEmail(String email) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Company.findByEmail", Company.class);
        q.setParameter("email", email);
        List<Company> rs = q.getResultList();
        return rs.isEmpty() ? null : rs.get(0);

    }

    @Override
    public Company getCompanyByTaxCode(String taxCode) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Company.findByTaxCode", Company.class);
        q.setParameter("taxCode", taxCode);
        List<Company> rs = q.getResultList();
        return rs.isEmpty() ? null : rs.get(0);

    }

}
