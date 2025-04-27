/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.myweb.pojo.Company;
import com.myweb.repositories.CompanyRepository;
import com.myweb.services.CompanyService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author dat
 */
@Service
public class CompanyServiceImplement implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Map<String, Object> getListCompany(Map<String, String> params) {
        return this.companyRepository.getListCompany(params);
    }

}
