/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.repositories.impl;

import com.myweb.dto.CompanyDTO;
import com.myweb.pojo.Company;
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
    public Map<String, Object> getListCompany(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();

        // Tạo CriteriaQuery để lấy dữ liệu từ Company và User
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Company> companyRoot = cq.from(Company.class);
        // Thực hiện join với User qua trường userId
        Root<User> userRoot = cq.from(User.class);

        
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(companyRoot.get("userId").get("id"), userRoot.get("id")));

        // Tìm kiếm
        if (params != null) {
            // Tên công ty (tìm kiếm gần đúng)
            String name = params.get("name");
            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(companyRoot.get("name"), String.format("%%%s%%", name)));
            }

            // Mã số thuế (tìm kiếm chính xác)
            String taxCode = params.get("taxCode");
            if (taxCode != null && !taxCode.isEmpty()) {
                predicates.add(cb.equal(companyRoot.get("taxCode"), taxCode));
            }

            // Trạng thái
            String status = params.get("status");
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(companyRoot.get("status"), status));
            }

            // Thành phố
            String city = params.get("city");
            if (city != null && !city.isEmpty()) {
                predicates.add(cb.equal(companyRoot.get("city"), city)); // Sửa cityCode thành city nếu đúng
            }

            // Quận/huyện
            String district = params.get("district");
            if (district != null && !district.isEmpty()) {
                predicates.add(cb.equal(companyRoot.get("district"), district)); // Sửa districtCode thành district nếu
                                                                                 // đúng
            }
        }

        // Áp dụng các điều kiện vào truy vấn
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(Predicate[]::new)));
        }

        // Chọn các trường cần lấy
        cq.multiselect(
                companyRoot.get("id"),
                companyRoot.get("name"),
                companyRoot.get("email"),
                companyRoot.get("avatar"),
                companyRoot.get("selfDescription"),
                companyRoot.get("taxCode"),
                companyRoot.get("fullAddress"),
                companyRoot.get("city"),
                companyRoot.get("district"),
                companyRoot.get("status"),
                userRoot.get("registerDate"),
                userRoot.get("isActive"));

        // Tạo truy vấn
        Query query = s.createQuery(cq);
        int totalRecords = query.getResultList().size();
        int page = 1, start = 1;

        // Phân trang
        page = Integer.parseInt(params.getOrDefault("page", GeneralUtils.PAGE));
        start = (page - 1) * GeneralUtils.PAGE_SIZE;
        query.setFirstResult(start);
        query.setMaxResults(GeneralUtils.PAGE_SIZE);

        // Lấy danh sách kết quả
        List<Object[]> rawResults = query.getResultList();

        // Chuyển đổi kết quả thành đối tượng DTO (nếu cần)
        List<CompanyDTO> results = rawResults.stream().map(row -> new CompanyDTO(
                (Integer) row[0], // id
                (String) row[1], // name
                (String) row[2], // email
                (String) row[3], // avatare
                (String) row[4], // selfDescription
                (String) row[5], // taxCode
                (String) row[6], // fullAddress
                (String) row[7], // city
                (String) row[8], // district
                (String) row[9], // status
                (Date)  row[10], // registerDate
                (Boolean) row[11] // isActive
        )).collect(Collectors.toList());

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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
