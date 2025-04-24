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
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author dat
 */
public class CompanyServiceImplement implements CompanyService {

    @Autowired
    private CompanyRepository compRe;
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Company addCompany(Company c) {
        try {
            Map r = this.cloudinary.uploader().upload(c.getFile().getBytes(), ObjectUtils.asMap("resource_type", "auto"));
            c.setAvatar((String) r.get("secure_url"));
            return this.compRe.addCompany(c);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return c;
    }

}
