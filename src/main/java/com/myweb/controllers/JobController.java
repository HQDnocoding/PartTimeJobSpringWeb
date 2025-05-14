package com.myweb.controllers;

import com.myweb.dto.CreateJobDTO;
import com.myweb.dto.GetJobDTO;
import com.myweb.pojo.Company;
import com.myweb.services.CompanyService;
import com.myweb.services.DayService;
import com.myweb.services.JobService;
import com.myweb.services.MajorService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping("/job")
public class JobController {

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobService jobService;

    @Autowired
    private MajorService majorService;

    @Autowired
    private DayService dayService;

    @Autowired
    private CompanyService companyService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(BigInteger.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);
                } else {
                    setValue(new BigInteger(text));
                }
            }
        });
    }

    @GetMapping
    public String listJobs(Model model, @RequestParam Map<String, String> params) {
        List<String> headCols = new ArrayList<>(List.of(
                "", "Tên công việc", "Công ty", "Lương", "Thành phố", "Ngành nghề", "Thời gian làm việc", "Ngày đăng", "Hành động"
        ));

        Map<String, Object> result = jobService.searchJobs(params);
        model.addAttribute("jobs", result.get("jobs"));
        model.addAttribute("currentPage", result.get("currentPage"));
        model.addAttribute("pageSize", result.get("pageSize"));
        model.addAttribute("totalPages", result.get("totalPages"));
        model.addAttribute("totalItems", result.get("totalItems"));
        model.addAttribute("headCols", headCols);

        model.addAttribute("majors", majorService.getMajors());
        model.addAttribute("days", dayService.getDays());

        return "job";
    }

    @GetMapping("/{id}")
    public String jobDetail(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        GetJobDTO job = jobService.getJobById(id);
        if (job == null) {
            logger.warn("Job not found with ID: {}", id);
            redirectAttributes.addFlashAttribute("errorMessage", "Công việc không tồn tại.");
            return "redirect:/job";
        }
        model.addAttribute("job", job);
        return "job-detail";
    }

    @GetMapping("/create-job")
    public String createJobView(Model model) {
        model.addAttribute("jobDTO", new CreateJobDTO());
        model.addAttribute("majors", majorService.getMajors());
        model.addAttribute("days", dayService.getDays());
        List<Company> companies = companyService.getAllCompaniesForDropdown();
        System.out.println("Number of companies passed to create-job: " + companies.size());
        model.addAttribute("companies", companies);
        return "create-job";
    }

    @PostMapping
    public String createJob(@Valid @ModelAttribute("jobDTO") CreateJobDTO jobDTO, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            logger.warn("Validation errors: {}", result.getAllErrors());
            model.addAttribute("majors", majorService.getMajors());
            model.addAttribute("days", dayService.getDays());
            List<Company> companies = companyService.getAllCompaniesForDropdown();
            System.out.println("Number of companies passed to create-job (on error): " + companies.size());
            model.addAttribute("companies", companies);
            model.addAttribute("errorMessage", result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining("; ")));
            return "create-job";
        }

        GetJobDTO createdJob = jobService.createJobDTO(jobDTO);
        logger.info("Created job with ID: {}", createdJob.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Tạo công việc thành công!");
        return "redirect:/job";
    }

    @DeleteMapping("/api/job/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") int id) {
        GetJobDTO job = jobService.getJobById(id);
        if (job == null) {
            logger.warn("Attempted to delete non-existent job with ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        jobService.deleteJob(id);
        logger.info("Deleted job with ID: {}", id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/job/delete")
    public String deleteJobs(@RequestParam(value = "jobIds", required = false) List<Integer> jobIds, Model model) {
        if (jobIds == null || jobIds.isEmpty()) {
            model.addAttribute("errorMessage", "Vui lòng chọn ít nhất một công việc để xóa!");
            return "redirect:/job";
        }

        for (Integer jobId : jobIds) {
            jobService.deleteJob(jobId);
        }
        model.addAttribute("successMessage", "Xóa " + jobIds.size() + " công việc thành công!");
        return "redirect:/job";
    }
}