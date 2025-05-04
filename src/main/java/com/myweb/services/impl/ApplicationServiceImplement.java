/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.services.impl;

import com.cloudinary.Cloudinary;
import com.myweb.dto.CreateApplicationDTO;
import com.myweb.pojo.Application;
import com.myweb.pojo.Candidate;
import com.myweb.pojo.Job;
import org.springframework.beans.factory.annotation.Autowired;

import com.myweb.repositories.ApplicationRepository;
import com.myweb.repositories.CandidateRepository;
import com.myweb.repositories.JobRepository;
import com.myweb.services.ApplicationService;
import com.myweb.utils.GeneralUtils;
import java.util.Date;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author huaquangdat
 */
@Service
public class ApplicationServiceImplement implements ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private Cloudinary cloudinary;

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

    @Override
    public Application addApplication(Application application) {

        if (application.getCurriculumVitaeFile()!= null && !application.getCurriculumVitaeFile().isEmpty()) {
            application.setCurriculumVitae(GeneralUtils.uploadFileToCloud(cloudinary, application.getCurriculumVitaeFile()));
        }
        application.setStatus(GeneralUtils.Status.pending.toString());
        application.setAppliedDate(new Date());
        try {
            return this.applicationRepository.addOrUpdateApplication(application);
        } catch (Exception e) {
            throw new IllegalArgumentException("Dữ liệu không hợp lệ, vui lòng kiểm tra lại thông tin.");

        }
    }

}
