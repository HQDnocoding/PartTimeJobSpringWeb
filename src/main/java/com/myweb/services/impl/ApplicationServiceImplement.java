/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.services.impl;

import com.myweb.pojo.Application;
import org.springframework.beans.factory.annotation.Autowired;

import com.myweb.repositories.ApplicationRepository;
import com.myweb.services.ApplicationService;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author huaquangdat
 */
@Service
public class ApplicationServiceImplement implements ApplicationService{
     @Autowired
    private ApplicationRepository applicationRepository;

    public Application addOrUpdateApplication(Application a) {
        return applicationRepository.addOrUpdateApplication(a);
    }

    public Map<String, Object> getListApplication(Map<String, String> params) {
        return applicationRepository.getListApplication(params);
    }

    public Application getApplicationById(int applicationId) {
        return applicationRepository.getApplicationById(applicationId);
    }

    @Override
    public void deleteApplication(int id) {
        this.applicationRepository.deleteApplication(id);
    }
    
}
