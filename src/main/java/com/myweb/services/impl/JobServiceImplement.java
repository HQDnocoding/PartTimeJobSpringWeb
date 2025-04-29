/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbformat/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.services.impl;

import com.myweb.pojo.Job;
import com.myweb.repositories.JobRepository;
import com.myweb.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JobServiceImplement implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Override
    public Map<String, Object> searchJobs(Map<String, String> params) {
        return jobRepository.searchJobs(params);
    }

    @Override
    public List<Job> getListJobByMajor(int majorId) {
        return jobRepository.getListJobByMajor(majorId);
    }

    @Override
    public Job getJobById(int jobId) {
        return jobRepository.getJobById(jobId);
    }

    @Override
    public List<Job> getListJobByRecommend(int majorId, int cityId) {
        return jobRepository.getListJobByRecommend(majorId, cityId);
    }

    @Override
    public List<Job> getListJobByCompanyId(int companyId) {
        return jobRepository.getListJobByCompanyId(companyId);
    }

    @Override
    public List<Job> getListJobByCompanyId1(int companyId) {
        return jobRepository.getListJobByCompanyId1(companyId);
    }

    @Override
    public List<Job> getListJobByCompanyExceptCurrentJob(int companyId, int jobId) {
        return jobRepository.getListJobByCompanyExceptCurrentJob(companyId, jobId);
    }

    @Override
    public Job getNameJob(int jobId) {
        return jobRepository.getNameJob(jobId);
    }

    @Override
    public List<Job> getListJobByCandidate(int candidateId) {
        return jobRepository.getListJobByCandidate(candidateId);
    }

    @Override
    public List<Job> getListJobByCheckAdmin() {
        return jobRepository.getListJobByCheckAdmin();
    }

    @Override
    public List<Job> getListJobByMajorAndCity(int majorId, String city, String kw) {
        return jobRepository.getListJobByMajorAndCity(majorId, city, kw);
    }

    @Override
    public List<Job> getListJobForManageCompany(int companyId) {
        return jobRepository.getListJobForManageCompany(companyId);
    }

    @Override
    public void updateJob(int jobId) {
        jobRepository.updateJob(jobId);
    }

    @Override
    public List<Job> getListJobByCityKw(String city, String kw) {
        return jobRepository.getListJobByCityKw(city, kw);
    }

    @Override
    public List<Job> getListJobByCityKwPage(String city, String kw, int page) {
        return jobRepository.getListJobByCityKwPage(city, kw, page);
    }

    @Override
    public Long countJob() {
        return jobRepository.countJob();
    }

    @Override
    public void deleteJob(int jobId) {
        jobRepository.deleteJob(jobId);
    }

    @Override
    public List<Job> getListJobForManage() {
        return jobRepository.getListJobForManage();
    }

    @Override
    public boolean addJob(Job j) {
        return jobRepository.addJob(j);
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.searchJobs(null).get("jobs") != null ?
                (List<Job>) jobRepository.searchJobs(null).get("jobs") : List.of();
    }
}