/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.myweb.dto.CreateCompanyDTO;
import com.myweb.pojo.Company;
import com.myweb.pojo.ImageWorkplace;
import com.myweb.pojo.User;
import com.myweb.repositories.CompanyRepository;
import com.myweb.services.CompanyService;
import com.myweb.utils.GeneralUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
     @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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

    @Override
    public void deleteCompany(int id) {
        this.companyRepository.deleteCompany(id);
    }

    @Override
    public Company createCompanyDTO(CreateCompanyDTO c) {
        User u = new User();
        u.setUsername(c.getUsername());
        u.setPassword(this.passwordEncoder.encode(c.getPassword()));
        u.setRegisterDate(new Date());
        u.setRole(GeneralUtils.Role.COMPANY.toString());
        u.setIsActive(true);

        Company company = new Company();
        company.setName(c.getName());
        company.setCity(c.getCity());
        company.setDistrict(c.getDistrict());
        company.setEmail(c.getEmail());
        company.setAvatar(GeneralUtils.uploadFileToCloud(cloudinary, c.getAvatarFile()));
        company.setSelfDescription(c.getSelfDescription());
        company.setFullAddress(c.getFullAddress());
        company.setTaxCode(c.getTaxCode());
        company.setStatus(GeneralUtils.Status.pending.toString());
        company.setUserId(u);

        List<ImageWorkplace> immageList = c.getFiles().stream().map(file
                -> new ImageWorkplace(GeneralUtils.uploadFileToCloud(cloudinary, file), company)).collect(Collectors.toList());

        return this.companyRepository.createCompanyDTO(u, company);

    }

}
