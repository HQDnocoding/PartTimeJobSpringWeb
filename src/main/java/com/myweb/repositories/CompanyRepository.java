/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.repositories;

import com.myweb.pojo.Company;
import com.myweb.pojo.ImageWorkplace;
import com.myweb.pojo.User;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dat
 */
public interface CompanyRepository {

    Map<String, Object> getListCompany(Map<String, String> params);

    Company addOrUpdateCompany(Company c);

    Company getCompanyById(int companyId);
    
    void deleteCompany(int id);
    
    
    Company createCompanyDTO(User u, Company c);
    

}
