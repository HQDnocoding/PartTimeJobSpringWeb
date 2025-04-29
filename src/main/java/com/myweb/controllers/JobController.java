/*
 * Click nbfs://.netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://.netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.controllers;

import com.myweb.pojo.Job;
import com.myweb.services.DayService;
import com.myweb.services.JobService;
import com.myweb.services.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
@ControllerAdvice
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private MajorService majorService;

    @Autowired
    private DayService dayService;

    @GetMapping
    public String listJobs(Model model, @RequestParam Map<String, String> params) {
        // Định nghĩa các cột tiêu đề cho bảng hiển thị công việc
        Collection<String> headCols = new ArrayList<>(List.of(
            "Tên công việc", "Mô tả", "Yêu cầu", "Lương tối thiểu", "Lương tối đa",
            "Địa chỉ", "Thành phố", "Quận", "Trạng thái", "Ngày đăng", "Công ty", "Ngành nghề", "Thời gian làm việc"
        ));

        // Gọi service để lấy danh sách công việc với tham số tìm kiếm và phân trang
        Map<String, Object> result = jobService.searchJobs(params);

        // Thêm dữ liệu vào model để gửi đến view
        model.addAttribute("jobs", result.get("jobs"));
        model.addAttribute("currentPage", result.get("currentPage"));
        model.addAttribute("pageSize", result.get("pageSize"));
        model.addAttribute("totalPages", result.get("totalPages"));
        model.addAttribute("totalItems", result.get("totalItems"));
        model.addAttribute("headCols", headCols);

        // Thêm danh sách ngành nghề và thời gian làm việc để hỗ trợ lọc
        model.addAttribute("majors", majorService.getMajors());
        model.addAttribute("days", dayService.getDays());

        return "job"; // Thay vì "job-list", trả về "job" để khớp với tên file job.html
    }

    @GetMapping("/{id}")
    public String jobDetail(@PathVariable("id") int id, Model model) {
        model.addAttribute("job", jobService.getJobById(id));
        model.addAttribute("majors", majorService.getMajors());
        model.addAttribute("days", dayService.getDays());
        return "job-detail";
    }
}