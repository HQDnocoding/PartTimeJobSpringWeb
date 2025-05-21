/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.repositories.impl;

import com.myweb.pojo.Company;
import com.myweb.pojo.Job;
import com.myweb.pojo.User;
import com.myweb.repositories.CompanyRepository;
import com.myweb.utils.GeneralUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;

import java.util.*;
import java.util.stream.Collectors;

import org.hibernate.HibernateException;

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

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> countCompaniesByMonth(Date startDate) {
        String query = "SELECT DATE_FORMAT(c.createdDate, '%Y-%m') as time, COUNT(c) as count "
                + "FROM Company c WHERE c.createdDate >= :startDate "
                + "GROUP BY DATE_FORMAT(c.createdDate, '%Y-%m')";
        return entityManager.createQuery(query, Object[].class)
                .setParameter("startDate", startDate)
                .getResultList();
    }

    // Thêm hoặc cập nhật công ty
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
            System.err.println(e.getStackTrace());
        }
        return c;
    }

    private String normalizeLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            return null;
        }
        // Loại bỏ tiền tố "Thành phố", "Tỉnh" và chuẩn hóa
        String normalized = location.trim()
                .replaceAll("^(Thành phố|Tỉnh)\\s*", "")
                .replaceAll("\\s+", " ");
        // Viết hoa chữ cái đầu, còn lại viết thường
        return normalized.substring(0, 1).toUpperCase() + normalized.substring(1).toLowerCase();
    }

    // Lấy danh sách công ty với lọc/phân trang
    @Override
    public Map<String, Object> getListCompany(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();

        CriteriaQuery<Company> cq = cb.createQuery(Company.class);
        Root<Company> companyRoot = cq.from(Company.class);

        List<Predicate> predicates = new ArrayList<>();

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
                city = normalizeLocation(city);
                if (city != null) {
                    predicates.add(cb.equal(cb.lower(companyRoot.get("city")), city.toLowerCase()));
                }
            }
            String district = params.get("district");
            if (district != null && !district.isEmpty()) {
                district = normalizeLocation(district);
                if (district != null) {
                    predicates.add(cb.equal(cb.lower(companyRoot.get("district")), district.toLowerCase()));
                }
            }
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(Predicate[]::new)));
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

        List<Company> results = query.getResultList();
        System.out.println("Results: " + results);

        int totalPages = (int) Math.ceil((double) totalRecords / GeneralUtils.PAGE_SIZE);

        Map<String, Object> result = new HashMap<>();
        result.put("companies", results);
        result.put("currentPage", page);
        result.put("pageSize", GeneralUtils.PAGE_SIZE);
        result.put("totalPages", totalPages);
        result.put("totalItems", totalRecords);

        return result;
    }

    // Lấy chi tiết công ty
    @Override
    public Company getCompanyById(int companyId) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Company.class, companyId);
    }

    @Override
    public Company getCompanyApproved(int companyId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();

        CriteriaQuery<Company> cq = cb.createQuery(Company.class);
        Root<Company> companyRoot = cq.from(Company.class);

        Join<Company, User> userJoin = companyRoot.join("userId");
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(companyRoot.get("id"), companyId));
        predicates.add(cb.equal(companyRoot.get("status"), "approved"));
        predicates.add(cb.equal(userJoin.get("isActive"), true));
        cq.where(predicates.toArray(Predicate[]::new));

        try {
            Company company = s.createQuery(cq).getSingleResult();

            return company;
        } catch (Exception e) {
            return null;
        }

    }

    // Xóa công ty và tài khoản người dùng
    @Override
    public void deleteCompany(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        Company c = this.getCompanyById(id);
        System.out.println(c);
        s.remove(c.getUserId());
    }

    // Tạo công ty và tài khoản người dùng
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

    // Kiểm tra công ty theo email
    @Override
    public Company getCompanyByEmail(String email) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Company.findByEmail", Company.class);
        q.setParameter("email", email);
        List<Company> rs = q.getResultList();
        return rs.isEmpty() ? null : rs.get(0);

    }

    // Kiểm tra công ty theo mã số thuế
    @Override
    public Company getCompanyByTaxCode(String taxCode) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("Company.findByTaxCode", Company.class);
        q.setParameter("taxCode", taxCode);
        List<Company> rs = q.getResultList();
        return rs.isEmpty() ? null : rs.get(0);

    }

    @Override
    public List<Company> getAllCompaniesForDropdown() {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Company> cq = cb.createQuery(Company.class);
        Root<Company> companyRoot = cq.from(Company.class);

        cq.orderBy(cb.asc(companyRoot.get("id")));

        List<Company> companies = s.createQuery(cq).getResultList();
        System.out.println("Total companies retrieved for dropdown: " + companies.size());
        return companies;
    }

    @Override
    public Collection<Job> getCompanyWithJobs(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();

        CriteriaQuery<Company> cq = cb.createQuery(Company.class);
        Root<Company> companyRoot = cq.from(Company.class);

        companyRoot.fetch("jobCollection", JoinType.LEFT);

        Join<Company, User> userJoin = companyRoot.join("userId");
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(companyRoot.get("id"), id));
        predicates.add(cb.equal(companyRoot.get("status"), "approved"));
        predicates.add(cb.equal(userJoin.get("isActive"), true));
        cq.where(predicates.toArray(Predicate[]::new));

        try {
            Company company = s.createQuery(cq).getSingleResult();
            return company.getJobCollection();

        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public User getUser(int id) {

        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();

        CriteriaQuery<Company> cq = cb.createQuery(Company.class);
        Root<Company> companyRoot = cq.from(Company.class);

        Join<Company, User> userJoin = companyRoot.join("userId");
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(companyRoot.get("id"), id));
        predicates.add(cb.equal(companyRoot.get("status"), "approved"));
        predicates.add(cb.equal(userJoin.get("isActive"), true));
        cq.where(predicates.toArray(Predicate[]::new));

        try {
            Company company = s.createQuery(cq).getSingleResult();
            return company.getUserId();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Company getCompanyByUserId(int userId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();

        CriteriaQuery<Company> cq = cb.createQuery(Company.class);
        Root<Company> companyRoot = cq.from(Company.class);
        Join<Company, User> userJoin = companyRoot.join("userId");
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(companyRoot.get("userId").get("id"), userId));
        predicates.add(cb.equal(companyRoot.get("status"), "approved"));
        predicates.add(cb.equal(userJoin.get("isActive"), true));
        cq.where(predicates.toArray(Predicate[]::new));

        try {
            Company company = s.createQuery(cq).getSingleResult();
            return company;
        } catch (Exception e) {
            return null;
        }
    }

}
