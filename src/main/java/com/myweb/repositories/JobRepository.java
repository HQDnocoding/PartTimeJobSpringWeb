package com.myweb.repositories;

import com.myweb.pojo.Job;
import java.util.List;
import java.util.Map;

public interface JobRepository {

    // Tìm kiếm công việc dựa trên các tiêu chí lọc với phân trang.
    Map<String, Object> searchJobs(Map<String, String> params);

    //  Lấy danh sách công việc liên quan đến một ngành nghề cụ thể.
    List<Job> getListJobByMajor(int majorId);

    // Lấy thông tin chi tiết của một công việc theo ID.
    Job getJobById(int jobId);

    // Lấy danh sách công việc được gợi ý dựa trên ngành nghề và thành phố.
    List<Job> getListJobByRecommend(int majorId, int cityId);

    // Lấy danh sách công việc của một công ty, bao gồm thông tin ngành nghề và ngày làm việc.
    List<Job> getListJobByCompanyId(int companyId);

    List<Job> getListJobByCompanyId1(int companyId);

    // Lấy danh sách công việc của một công ty, ngoại trừ một công việc cụ thể.
    List<Job> getListJobByCompanyExceptCurrentJob(int companyId, int jobId);

    // Lấy thông tin công việc theo ID, chủ yếu để lấy tên công việc.
    Job getNameJob(int jobId);

    // Lấy danh sách công việc mà một ứng viên đã ứng tuyển.
    List<Job> getListJobByCandidate(int candidateId);

    // Lấy danh sách công việc đang chờ quản trị viên phê duyệt.
    List<Job> getListJobByCheckAdmin();

    // Lấy danh sách công việc dựa trên ngành nghề, thành phố và từ khóa.
    List<Job> getListJobByMajorAndCity(int majorId, String city, String kw);

    // Lấy danh sách công việc để quản lý bởi công ty.
    List<Job> getListJobForManageCompany(int companyId);

    // Cập nhật thông tin của một công việc.
    void updateJob(int jobId);

    // Lấy danh sách công việc theo thành phố và từ khóa.
    List<Job> getListJobByCityKw(String city, String kw);

    // Lấy danh sách công việc theo thành phố, từ khóa và phân trang.
    List<Job> getListJobByCityKwPage(String city, String kw, int page);

    // Đếm tổng số công việc trong hệ thống.
    Long countJob();

    // Xóa một công việc theo ID.
    void deleteJob(int jobId);

    // Lấy danh sách công việc để quản lý (thường dành cho quản trị viên).
    List<Job> getListJobForManage();

    // Thêm một công việc mới.
    boolean addJob(Job j);

    Job addJobDTO(Job j);

    void addDaysToJob(Job job, List<Integer> dayIds);

    //dat
    Job getOnlyJobById(int id);

    List<Job> getJobByAuthenticateCompany(int companyId);

//    List<Job> getJobListByAuthenticateCandidate(int candidateId);
}
