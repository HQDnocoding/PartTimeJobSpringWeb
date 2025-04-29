/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.controllers;

import com.myweb.services.ApplicationService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author huaquangdat
 */

@Controller
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/applications")
    public String applicationView(Model model, @RequestParam Map<String, String> params) {
        // Định nghĩa tiêu đề cột cho bảng
        Collection<String> headCols = new ArrayList<>(List.of(
            "Id","Ứng viên", "Ngày ứng tuyển", "Lời nhắn", "Trạng thái",  "Công việc"
        ));

        // Gọi service để lấy danh sách đơn ứng tuyển
        Map<String, Object> result = applicationService.getListApplication(params);

        // Thêm dữ liệu vào model
        model.addAttribute("applications",  result.get("applications"));
        model.addAttribute("currentPage", result.get("currentPage"));
        model.addAttribute("pageSize", result.get("pageSize"));
        model.addAttribute("totalPages", result.get("totalPages"));
        model.addAttribute("totalItems", result.get("totalItems"));
        model.addAttribute("headCols", headCols);

        // Log để debug
        System.out.println("Applications: " + result.get("applications"));

        return "application"; // Tên template Thymeleaf
    }
}
