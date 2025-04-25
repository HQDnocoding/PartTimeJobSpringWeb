/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.myweb.pojo.Candidate;
import com.myweb.pojo.Company;
import com.myweb.pojo.User;
import com.myweb.repositories.UserRepository;
import com.myweb.services.UserService;
import com.myweb.utils.GeneralUtils;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author huaquangdat
 */
@Service("userDetailsService")
public class UserServiceImplement implements UserService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public User getUserByUsername(String username) {
        return this.userRepo.getUserByUserName(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = this.getUserByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Invalid username!");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(u.getRole()));

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(), u.getPassword(), authorities);
    }

    @Override
    public User addUser(Map<String, String> params, MultipartFile avatar) {

//        switch (params.get("role")) {
//            case "ROLE_CANDIDATE":
//                Candidate candidate=new Candidate();
//                candidate.setFullName(params.get("fullname"));
//                candidate.setUsername(params.get("username"));
//                candidate.setPassword(this.passwordEncoder.encode(params.get("password")));
//                candidate.setEmail(params.get("email"));
//                candidate.setCity(params.get("city"));
//                candidate.set(LocalDate.pparams.get("birthDay"));
//                
//                break;
//            case "ROLE_COMPANY":
//                Company company = new Company();
//                company.setName(params.get("name"));
//                company.setCity(params.get("city"));
//                company.setDistrict(params.get("district"));
//                company.setUsername(params.get("username"));
//                company.setPassword(this.passwordEncoder.encode(params.get("password")));
//                company.setEmail(params.get("email"));
//                company.setTaxCode(params.get("tax"));
//                company.setFullAddress(params.get("fullAddress"));
//                company.setSelfDescription(params.get("seftDes"));
//                company.setStatus(GeneralUtils.Status.pending.toString());
//                company.setRegisterDate(LocalDateTime.now());
//
//                if (!avatar.isEmpty()) {
//                    try {
//                        Map res = cloudinary.uploader().upload(avatar.getBytes(),
//                                ObjectUtils.asMap("resource_type", "auto"));
//                        company.setAvatar(res.get("secure_url").toString());
//                    } catch (IOException ex) {
//                        Logger.getLogger(UserServiceImplement.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//
//                return this.userRepo.addUser(company);
//        }

        return null;
    }

}