/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.repositories;

import com.myweb.dto.JobDTO;
import com.myweb.pojo.Job;

import java.util.List;
import java.util.Map;

public interface JobRepository {
    Map<String, Object> searchJobs(Map<String, String> params);
    List<JobDTO> getListJobByMajor(int majorId);
    JobDTO getDetailJobById(int jobId);
    List<JobDTO> getListJobByRecommend(int majorId, int cityId);
    List<Job> getListJobByCompanyId(int companyId);
    List<JobDTO> getListJobByCompanyId1(int companyId);
    List<JobDTO> getListJobByCompanyExceptCurrentJob(int companyId, int jobId);
    Job getNameJob(int jobId);
    List<JobDTO> getListJobByCandidate(int candidateId);
    List<JobDTO> getListJobByCheckAdmin();
    List<JobDTO> getListJobByMajorAndCity(int majorId, String city, String kw);
    List<Job> getListJobForManageCompany(int companyId);
    void updateJob(int jobId);
    List<JobDTO> getListJobByCityKw(String city, String kw);
    List<JobDTO> getListJobByCityKwPage(String city, String kw, int page);
    Long countJob();
    void deleteJob(int jobId);
    List<JobDTO> getListJobForManage();
    boolean addJob(Job j);
}
