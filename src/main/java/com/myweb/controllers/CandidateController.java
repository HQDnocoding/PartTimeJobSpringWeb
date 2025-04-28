/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.controllers;

import com.myweb.pojo.Candidate;
import com.myweb.services.CandidateService;
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
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @GetMapping("/candidates")
    public String candidateView(Model model, @RequestParam Map<String, String> params) {
        // Định nghĩa tiêu đề cột cho bảng
        Collection<String> headCols = new ArrayList<>(List.of(
            "Id", "Họ tên", "Email", "Ngày sinh", "Thành phố", "Số điện thoại"
        ));

        // Gọi service để lấy danh sách ứng viên
        Map<String, Object> result = candidateService.getListCandidate(params);

        // Thêm dữ liệu vào model
        model.addAttribute("candidates", (List<Candidate>) result.get("candidates"));
        model.addAttribute("currentPage", result.get("currentPage"));
        model.addAttribute("pageSize", result.get("pageSize"));
        model.addAttribute("totalPages", result.get("totalPages"));
        model.addAttribute("totalItems", result.get("totalItems"));
        model.addAttribute("headCols", headCols);

        // Log để debug
        System.out.println("Candidates: " + result.get("candidates"));

        return "candidate"; // Tên template Thymeleaf
    }
}
