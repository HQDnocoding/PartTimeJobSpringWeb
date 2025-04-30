/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.services;

import com.myweb.dto.CreateCompanyDTO;
import com.myweb.pojo.Company;
import jakarta.data.page.Page;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author dat
 */

public interface CompanyService {

    Map<String, Object> getListCompany(Map<String, String> params);
    
    Company getCompany(int companyId);
    
    Company addOrUpdate(Company p);

    
    void deleteCompany(int id);
    
    Company createCompanyDTO(CreateCompanyDTO c);
}
