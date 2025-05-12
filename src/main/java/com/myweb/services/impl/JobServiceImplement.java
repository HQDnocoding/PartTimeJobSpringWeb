package com.myweb.services.impl;

import com.myweb.dto.CreateJobDTO;
import com.myweb.dto.GetJobDTO;
import com.myweb.pojo.Company;
import com.myweb.pojo.Day;
import com.myweb.pojo.Job;
import com.myweb.pojo.Major;
import com.myweb.pojo.MajorJob;
import com.myweb.repositories.CompanyRepository;
import com.myweb.repositories.DayRepository;
import com.myweb.repositories.JobRepository;
import com.myweb.repositories.MajorRepository;
import com.myweb.services.JobService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class JobServiceImplement implements JobService {
    
    private static final Logger logger = Logger.getLogger(JobServiceImplement.class.getName());
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private MajorRepository majorRepository;
    
    @Autowired
    private DayRepository dayRepository;
    
    private void initializeJobCollections(Job job) {
        if (job != null && Hibernate.isInitialized(job)) {
            Hibernate.initialize(job.getMajorJobCollection());
            Hibernate.initialize(job.getDayJobCollection());
        }
    }
    
    private void initializeJobCollections(List<Job> jobs) {
        if (jobs != null) {
            for (Job job : jobs) {
                initializeJobCollections(job);
            }
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> searchJobs(Map<String, String> params) {
        Map<String, Object> result = jobRepository.searchJobs(params);
        List<Job> jobs = (List<Job>) result.get("jobs");
        initializeJobCollections(jobs);
        return result;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Job> getListJobByMajor(int majorId) {
        List<Job> jobs = jobRepository.getListJobByMajor(majorId);
        initializeJobCollections(jobs);
        return jobs;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Job getJobById(int jobId) {
        Job job = jobRepository.getJobById(jobId);
        initializeJobCollections(job);
        return job;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Job> getListJobByRecommend(int majorId, int cityId) {
        List<Job> jobs = jobRepository.getListJobByRecommend(majorId, cityId);
        initializeJobCollections(jobs);
        return jobs;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Job> getListJobByCompanyId(int companyId) {
        List<Job> jobs = jobRepository.getListJobByCompanyId(companyId);
        initializeJobCollections(jobs);
        return jobs;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Job> getListJobByCompanyId1(int companyId) {
        List<Job> jobs = jobRepository.getListJobByCompanyId1(companyId);
        initializeJobCollections(jobs);
        return jobs;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Job> getListJobByCompanyExceptCurrentJob(int companyId, int jobId) {
        List<Job> jobs = jobRepository.getListJobByCompanyExceptCurrentJob(companyId, jobId);
        initializeJobCollections(jobs);
        return jobs;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Job getNameJob(int jobId) {
        Job job = jobRepository.getNameJob(jobId);
        initializeJobCollections(job);
        return job;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Job> getListJobByCandidate(int candidateId) {
        List<Job> jobs = jobRepository.getListJobByCandidate(candidateId);
        initializeJobCollections(jobs);
        return jobs;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Job> getListJobByCheckAdmin() {
        List<Job> jobs = jobRepository.getListJobByCheckAdmin();
        initializeJobCollections(jobs);
        return jobs;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Job> getListJobByMajorAndCity(int majorId, String city, String kw) {
        List<Job> jobs = jobRepository.getListJobByMajorAndCity(majorId, city, kw);
        initializeJobCollections(jobs);
        return jobs;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Job> getListJobForManageCompany(int companyId) {
        List<Job> jobs = jobRepository.getListJobForManageCompany(companyId);
        initializeJobCollections(jobs);
        return jobs;
    }
    
    @Override
    public void updateJob(int jobId) {
        jobRepository.updateJob(jobId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Job> getListJobByCityKw(String city, String kw) {
        List<Job> jobs = jobRepository.getListJobByCityKw(city, kw);
        initializeJobCollections(jobs);
        return jobs;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Job> getListJobByCityKwPage(String city, String kw, int page) {
        List<Job> jobs = jobRepository.getListJobByCityKwPage(city, kw, page);
        initializeJobCollections(jobs);
        return jobs;
    }
    
    @Override
    public Long countJob() {
        return jobRepository.countJob();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Job> getListJobForManage() {
        List<Job> jobs = jobRepository.getListJobForManage();
        initializeJobCollections(jobs);
        return jobs;
    }
    
    @Override
    public boolean addJob(Job j) {
        return jobRepository.addJob(j);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Job> getAllJobs() {
        List<Job> jobs = jobRepository.searchJobs(null).get("jobs") != null
                ? (List<Job>) jobRepository.searchJobs(null).get("jobs") : List.of();
        initializeJobCollections(jobs);
        return jobs;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Job> getJobList() {
        List<Job> jobs = jobRepository.getListJobForManage();
        initializeJobCollections(jobs);
        return jobs;
    }
    
    @Override
    @Transactional
    public GetJobDTO createJobDTO(CreateJobDTO jobDTO) {
        logger.info("Starting job creation for: " + jobDTO.getJobName());

        // Validate Company
        logger.info("Validating company ID: " + jobDTO.getCompanyId());
        Company company = companyRepository.getCompanyById(jobDTO.getCompanyId());
        if (company == null) {
            logger.warning("Invalid company ID: " + jobDTO.getCompanyId());
            throw new IllegalArgumentException("Công ty không tồn tại.");
        }
        if (!"approved".equals(company.getStatus())) {
            logger.warning("Company not approved: " + company.getId());
            throw new IllegalArgumentException("Công ty chưa được phê duyệt.");
        }

        // Validate Major
        logger.info("Validating major ID: " + jobDTO.getMajorId());
        Major major = majorRepository.getMajorById(jobDTO.getMajorId());
        if (major == null) {
            logger.warning("Invalid major ID: " + jobDTO.getMajorId());
            throw new IllegalArgumentException("Ngành nghề không tồn tại.");
        }

        // Validate Day IDs
        logger.info("Validating day IDs: " + jobDTO.getDayIds());
        List<Day> days = dayRepository.getDays();
        List<Integer> validDayIds = days.stream().map(Day::getId).toList();
        if (jobDTO.getDayIds() == null || jobDTO.getDayIds().stream().anyMatch(id -> !validDayIds.contains(id))) {
            logger.warning("Invalid day IDs: " + jobDTO.getDayIds());
            throw new IllegalArgumentException("Thời gian làm việc không hợp lệ.");
        }

        // Create Job entity
        logger.info("Creating Job entity");
        Job job = new Job();
        job.setJobName(jobDTO.getJobName());
        job.setCompanyId(company);
        job.setSalaryMin(jobDTO.getSalaryMin());
        job.setSalaryMax(jobDTO.getSalaryMax());
        

        // Lấy fullAddress và city từ company (không dùng dữ liệu từ jobDTO vì form đã disable)
        String fullAddress = company.getFullAddress();
        String city = company.getCity();
        String district = company.getDistrict();

        //Gán tọa độ
        job.setLongitude(jobDTO.getLongitude());
        job.setLatitude(jobDTO.getLatitude());
        
        

        // Kiểm tra dữ liệu từ company
        if (fullAddress == null || fullAddress.trim().isEmpty()) {
            logger.warning("Company fullAddress is null or empty for company ID: " + company.getId());
            throw new IllegalArgumentException("Địa chỉ công ty không được để trống.");
        }
        if (city == null || city.trim().isEmpty()) {
            logger.warning("Company city is null or empty for company ID: " + company.getId());
            throw new IllegalArgumentException("Thành phố công ty không được để trống.");
        }
        if (district == null || district.trim().isEmpty()) {
            logger.info("Company district is null or empty, setting to default: Không xác định");
            district = "Không xác định";
        }
        
        logger.info("Setting address: fullAddress=" + fullAddress + ", city=" + city + ", district=" + district);
        job.setFullAddress(fullAddress);
        job.setCity(city);
        job.setDistrict(district);
        
        job.setDescription(jobDTO.getDescription());
        job.setJobRequired(jobDTO.getJobRequired());
        job.setStatus("pending");
        job.setIsActive(true);
        job.setPostedDate(new Date());

        // Create MajorJob
        logger.info("Creating MajorJob");
        MajorJob majorJob = new MajorJob();
        majorJob.setJobId(job);
        majorJob.setMajorId(major);
        Set<MajorJob> majorJobs = new HashSet<>();
        majorJobs.add(majorJob);
        job.setMajorJobCollection(majorJobs);

        // Save Job
        try {
            logger.info("Saving job to database");
            job = jobRepository.addJobDTO(job); // Gán lại job từ kết quả trả về
            if (job == null || job.getId() == null) {
                logger.severe("Failed to save job or retrieve job ID");
                throw new IllegalStateException("Không thể tạo công việc do lỗi lưu trữ.");
            }
            
            logger.info("Job created successfully with ID: " + job.getId());

            // Save DayJob using repository method
            jobRepository.addDaysToJob(job, jobDTO.getDayIds());
            
            return new GetJobDTO(job);
        } catch (Exception e) {
            logger.severe("Error saving job: " + e.getMessage() + ", StackTrace: " + getStackTrace(e));
            throw new RuntimeException("Lỗi lưu công việc: " + e.getMessage(), e);
        }
    }
    
    private String getStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
    
    @Override
    public void deleteJob(int jobId) {
        jobRepository.deleteJob(jobId);
    }
}
