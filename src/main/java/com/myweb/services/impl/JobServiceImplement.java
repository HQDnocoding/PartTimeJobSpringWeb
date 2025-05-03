/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbformat/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.services.impl;

import com.myweb.pojo.Job;
import com.myweb.repositories.JobRepository;
import com.myweb.services.JobService;
import org.hibernate.Hibernate;
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
        Map<String, Object> result = jobRepository.searchJobs(params);
        List<Job> jobs = (List<Job>) result.get("jobs");
        // Initialize collections for each job
        if (jobs != null) {
            for (Job job : jobs) {
                Hibernate.initialize(job.getMarjorJobCollection());
                Hibernate.initialize(job.getDayJobCollection());
            }
        }
        return result;
    }

    @Override
    public List<Job> getListJobByMajor(int majorId) {
        List<Job> jobs = jobRepository.getListJobByMajor(majorId);
        // Initialize collections for each job
        for (Job job : jobs) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }
        return jobs;
    }

    @Override
    public Job getJobById(int jobId) {
        Job job = jobRepository.getJobById(jobId);
        if (job != null) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }
        return job;
    }

    @Override
    public List<Job> getListJobByRecommend(int majorId, int cityId) {
        List<Job> jobs = jobRepository.getListJobByRecommend(majorId, cityId);
        for (Job job : jobs) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }
        return jobs;
    }

    @Override
    public List<Job> getListJobByCompanyId(int companyId) {
        List<Job> jobs = jobRepository.getListJobByCompanyId(companyId);
        for (Job job : jobs) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }
        return jobs;
    }

    @Override
    public List<Job> getListJobByCompanyId1(int companyId) {
        List<Job> jobs = jobRepository.getListJobByCompanyId1(companyId);
        for (Job job : jobs) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }
        return jobs;
    }

    @Override
    public List<Job> getListJobByCompanyExceptCurrentJob(int companyId, int jobId) {
        List<Job> jobs = jobRepository.getListJobByCompanyExceptCurrentJob(companyId, jobId);
        for (Job job : jobs) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }
        return jobs;
    }

    @Override
    public Job getNameJob(int jobId) {
        Job job = jobRepository.getNameJob(jobId);
        if (job != null) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }
        return job;
    }

    @Override
    public List<Job> getListJobByCandidate(int candidateId) {
        List<Job> jobs = jobRepository.getListJobByCandidate(candidateId);
        for (Job job : jobs) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }
        return jobs;
    }

    @Override
    public List<Job> getListJobByCheckAdmin() {
        List<Job> jobs = jobRepository.getListJobByCheckAdmin();
        for (Job job : jobs) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }
        return jobs;
    }

    @Override
    public List<Job> getListJobByMajorAndCity(int majorId, String city, String kw) {
        List<Job> jobs = jobRepository.getListJobByMajorAndCity(majorId, city, kw);
        for (Job job : jobs) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }
        return jobs;
    }

    @Override
    public List<Job> getListJobForManageCompany(int companyId) {
        List<Job> jobs = jobRepository.getListJobForManageCompany(companyId);
        for (Job job : jobs) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }
        return jobs;
    }

    @Override
    public void updateJob(int jobId) {
        jobRepository.updateJob(jobId);
    }

    @Override
    public List<Job> getListJobByCityKw(String city, String kw) {
        List<Job> jobs = jobRepository.getListJobByCityKw(city, kw);
        for (Job job : jobs) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }
        return jobs;
    }

    @Override
    public List<Job> getListJobByCityKwPage(String city, String kw, int page) {
        List<Job> jobs = jobRepository.getListJobByCityKwPage(city, kw, page);
        for (Job job : jobs) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }
        return jobs;
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
        List<Job> jobs = jobRepository.getListJobForManage();
        for (Job job : jobs) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }
        return jobs;
    }

    @Override
    public boolean addJob(Job j) {
        return jobRepository.addJob(j);
    }

    @Override
    public List<Job> getAllJobs() {
        List<Job> jobs = jobRepository.searchJobs(null).get("jobs") != null ?
                (List<Job>) jobRepository.searchJobs(null).get("jobs") : List.of();
        for (Job job : jobs) {
            Hibernate.initialize(job.getMarjorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }
        return jobs;
    }

    //Dat codes
    @Override
    public List<Job> getJobList() {
        return this.jobRepository.getListJobForManage();
    }
    
    
}