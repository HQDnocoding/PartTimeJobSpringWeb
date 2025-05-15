package com.myweb.services;

import com.myweb.dto.CreateJobDTO;
import com.myweb.dto.GetJobDTO;
import com.myweb.pojo.Job;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface JobService {

    Map<String, Object> searchJobs(Map<String, String> params);

    List<GetJobDTO> getListJobByMajor(int majorId);

    GetJobDTO getJobById(int jobId);

    List<GetJobDTO> getListJobByRecommend(int majorId, int cityId);

    List<GetJobDTO> getListJobByCompanyId(int companyId);

    List<GetJobDTO> getListJobByCompanyId1(int companyId);

    List<GetJobDTO> getListJobByCompanyExceptCurrentJob(int companyId, int jobId);

    GetJobDTO getNameJob(int jobId);

    List<GetJobDTO> getListJobByCandidate(int candidateId);

    List<GetJobDTO> getListJobByCheckAdmin();

    List<GetJobDTO> getListJobByMajorAndCity(int majorId, String city, String kw);

    List<GetJobDTO> getListJobForManageCompany(int companyId);

    void updateJob(int jobId);

    List<GetJobDTO> getListJobByCityKw(String city, String kw);

    List<GetJobDTO> getListJobByCityKwPage(String city, String kw, int page);

    Long countJob();

    List<GetJobDTO> getListJobForManage();

    boolean addJob(Job j);

    List<GetJobDTO> getAllJobs();

    List<GetJobDTO> getJobList();

    GetJobDTO createJobDTO(CreateJobDTO jobDTO);

    void deleteJob(int jobId);

    //dat
    Job getOnlyJobById(int id);

    List<Job> getJobByAuthenticateCompany(Principal principal);
}
