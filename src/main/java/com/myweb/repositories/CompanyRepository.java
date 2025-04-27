/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.repositories;

import com.myweb.pojo.Company;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dat
 */
public interface CompanyRepository {

    Map<String, Object> getListCompany(Map<String, String> params);

    Company addCompany(Company c);

    Company getCompanyById(int companyId);
//    List<Object[]> getListCompanyByCity(String city);
//    List<Object[]> getListCompanyBycityAndCompany(String company, String city);
//    List<Object[]> getListCompanyByCompany(String company);
//    long countCompany();
//    List<Company> getListCompany();
//    List<Company> getListCompanyByCheckAdmin();
//    void checkCompany(int companyID);
//    List<Company> getListCompanyForManage();
}
