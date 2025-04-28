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
import java.util.logging.Level;
import java.util.logging.Logger;
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

    @Override
    public Company getCompany(int companyId) {
        return this.companyRepository.getCompanyById(companyId);
    }

    @Override
    public Company addOrUpdate(Company c) {
//        if (!c.getFile().isEmpty()) {
//            try {
//                Map res = cloudinary.uploader().upload(c.getFile().getBytes(), ObjectUtils.asMap("resource_type", "auto"));
//                c.setAvatar(res.get("secure_url").toString());
//            } catch (IOException ex) {
//                Logger.getLogger(CompanyServiceImplement.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }

        return this.companyRepository.addOrUpdateCompany(c);
    }

}
