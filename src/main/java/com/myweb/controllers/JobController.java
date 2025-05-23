package com.myweb.controllers;

import com.myweb.pojo.Company;
import com.myweb.pojo.Job;
import com.myweb.pojo.Major;
import com.myweb.pojo.MajorJob;
import com.myweb.pojo.Day;
import com.myweb.pojo.DayJob;
import com.myweb.services.CompanyService;
import com.myweb.services.DayService;
import com.myweb.services.JobService;
import com.myweb.services.MajorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.math.BigInteger;
import java.util.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class JobController {

    @Autowired
    private JobService jobService;
    @Autowired
    private MajorService majorService;
    @Autowired
    private DayService dayService;
    @Autowired
    private CompanyService companyService;

    // Lấy danh sách công việc với bộ lọc và phân trang
    @GetMapping("/jobs")
    public String listJobs(Model model, @RequestParam Map<String, String> params) {
        Map<String, Object> result = jobService.searchJobs(params);
        model.addAttribute("jobs", result.get("jobs"));
        model.addAttribute("currentPage", result.get("currentPage"));
        model.addAttribute("pageSize", result.get("pageSize"));
        model.addAttribute("totalPages", result.get("totalPages"));
        model.addAttribute("totalItems", result.get("totalItems"));
        model.addAttribute("headCols", List.of("", "Tên công việc", "Công ty", "Lương", "Thành phố", "Ngành nghề", "Thời gian làm việc", "Ngày đăng", "Hành động"));
        model.addAttribute("majors", majorService.getMajors());
        model.addAttribute("days", dayService.getDays());
        return "job";
    }

    // Hiển thị chi tiết công việc theo ID
    @GetMapping("/jobs/{id}")
    public String jobDetail(@PathVariable("id") int id, Model model) {
        Job job = jobService.getJobById(id);
        model.addAttribute("job", job);
        model.addAttribute("majors", majorService.getMajors());
        model.addAttribute("days", dayService.getDays());
        model.addAttribute("companies", companyService.getAllCompaniesForDropdown());
        return "job-detail";
    }

    // Hiển thị form tạo công việc mới
    @GetMapping("/jobs/create-job")
    public String createJobView(Model model) {
        model.addAttribute("jobDTO", new com.myweb.dto.CreateJobDTO());
        model.addAttribute("majors", majorService.getMajors());
        model.addAttribute("days", dayService.getDays());
        model.addAttribute("companies", companyService.getAllCompaniesForDropdown());
        return "create-job";
    }

    // Xử lý tạo công việc mới
    @PostMapping("/jobs")
    public String createJob(@Valid @ModelAttribute("jobDTO") com.myweb.dto.CreateJobDTO jobDTO, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("majors", majorService.getMajors());
            model.addAttribute("days", dayService.getDays());
            model.addAttribute("companies", companyService.getAllCompaniesForDropdown());
            return "create-job";
        }
        jobService.addJob(jobDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Tạo công việc thành công!");
        return "redirect:/jobs";
    }

    // Xử lý xóa nhiều công việc
    @PostMapping("/jobs/delete")
    public String deleteJobs(@RequestParam("jobIds") List<Integer> jobIds, Model model) {
        for (Integer jobId : jobIds) {
            jobService.deleteJob(jobId);
        }
        return "redirect:/jobs";
    }

    // Xử lý cập nhật công việc
    @PostMapping("/jobs/{jobId}/update")
    public String updateJob(
            @PathVariable("jobId") int jobId,
            @RequestParam("jobName") String jobName,
            @RequestParam("companyId") Integer companyId,
            @RequestParam("salaryMin") BigInteger salaryMin,
            @RequestParam("salaryMax") BigInteger salaryMax,
            @RequestParam(value = "majorId", required = false) Integer majorId,
            @RequestParam(value = "dayIds", required = false) List<Integer> dayIds,
            @RequestParam("description") String description,
            @RequestParam("jobRequired") String jobRequired,
            @RequestParam(value = "ageFrom", required = false) Integer ageFrom,
            @RequestParam(value = "ageTo", required = false) Integer ageTo,
            @RequestParam(value = "experienceRequired", required = false) Integer experienceRequired,
            @RequestParam("status") String status,
            @RequestParam("isActive") boolean isActive,
            RedirectAttributes redirectAttributes) {

        // Validate required fields
        if (jobName == null || jobName.trim().isEmpty() || companyId == null || salaryMin == null || salaryMax == null ||
            majorId == null || dayIds == null || dayIds.isEmpty() || description == null || description.trim().isEmpty() ||
            jobRequired == null || jobRequired.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng điền đầy đủ các trường bắt buộc.");
            return "redirect:/jobs/" + jobId;
        }

        // Validate salary range
        if (salaryMax.compareTo(salaryMin) < 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lương không hợp lệ.");
            return "redirect:/jobs/" + jobId;
        }

        Job existingJob = jobService.getOnlyJobById(jobId);
        if (existingJob == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Công việc không tồn tại.");
            return "redirect:/jobs/" + jobId;
        }

        existingJob.setJobName(jobName);

        // Xử lý companyId
        Company company = companyService.getCompany(companyId);
        if (company == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Công ty không tồn tại.");
            return "redirect:/jobs/" + jobId;
        }
        existingJob.setCompanyId(company);
        existingJob.setFullAddress(company.getFullAddress());
        existingJob.setCity(company.getCity());
        existingJob.setDistrict(company.getDistrict());

        existingJob.setSalaryMin(salaryMin);
        existingJob.setSalaryMax(salaryMax);
        existingJob.setDescription(description);
        existingJob.setJobRequired(jobRequired);
        existingJob.setAgeFrom(ageFrom);
        existingJob.setAgeTo(ageTo);
        existingJob.setExperienceRequired(experienceRequired);
        existingJob.setStatus(status);
        existingJob.setIsActive(isActive);
        existingJob.setPostedDate(new Date());

        // Xử lý MajorJob
        jobService.deleteMajorJobsByJobId(jobId);
        Collection<MajorJob> majorJobs = new ArrayList<>();
        Major major = majorService.getMajorById(majorId);
        if (major != null) {
            MajorJob majorJob = new MajorJob();
            majorJob.setJobId(existingJob);
            majorJob.setMajorId(major);
            majorJobs.add(majorJob);
        }
        existingJob.setMajorJobCollection(majorJobs);

        // Xử lý DayJob
        jobService.deleteDayJobsByJobId(jobId);
        Collection<DayJob> dayJobs = new ArrayList<>();
        for (Integer dayId : dayIds) {
            Day day = dayService.getDayById(dayId);
            if (day != null) {
                DayJob dayJob = new DayJob();
                dayJob.setJobId(existingJob);
                dayJob.setDayId(day);
                dayJobs.add(dayJob);
            }
        }
        existingJob.setDayJobCollection(dayJobs);

        jobService.addOrUpdateJob(existingJob);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật công việc thành công!");
        return "redirect:/jobs/" + jobId;
    }
}