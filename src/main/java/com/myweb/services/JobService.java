/*
 * Click nbfs://.netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://.netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.services;

import com.myweb.pojo.Job;
import java.util.List;
import java.util.Map;

public interface JobService {
    Map<String, Object> searchJobs(Map<String, String> params);
    List<Job> getListJobByMajor(int majorId);
    Job getJobById(int jobId);
    List<Job> getListJobByRecommend(int majorId, int cityId);
    List<Job> getListJobByCompanyId(int companyId);
    List<Job> getListJobByCompanyId1(int companyId);
    List<Job> getListJobByCompanyExceptCurrentJob(int companyId, int jobId);
    Job getNameJob(int jobId);
    List<Job> getListJobByCandidate(int candidateId);
    List<Job> getListJobByCheckAdmin();
    List<Job> getListJobByMajorAndCity(int majorId, String city, String kw);
    List<Job> getListJobForManageCompany(int companyId);
    void updateJob(int jobId);
    List<Job> getListJobByCityKw(String city, String kw);
    List<Job> getListJobByCityKwPage(String city, String kw, int page);
    Long countJob();
    void deleteJob(int jobId);
    List<Job> getListJobForManage();
    boolean addJob(Job j);

    // Thêm phương thức để khớp với JobController
    List<Job> getAllJobs();
}