/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.controllers;

import com.myweb.pojo.Company;
import com.myweb.services.CompanyService;
import jakarta.data.page.Page;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author huaquangdat
 */
@Controller
@ControllerAdvice
public class CompanyController {

    @Autowired
    private CompanyService cpnyService;

    @GetMapping("/company")
   public String companyView(Model model, @RequestParam Map<String, String> params) {
        Collection<String> headCols = new ArrayList<>(List.of("Id", "Tên công ty", "Email", "Địa chỉ", "Mã số thuế","Ngày đăng ký", "Trạng thái"));

        Map<String, Object> result = cpnyService.getListCompany(params);

        model.addAttribute("companies", result.get("companies"));
        model.addAttribute("currentPage", result.get("currentPage"));
        model.addAttribute("pageSize", result.get("pageSize"));
        model.addAttribute("totalPages", result.get("totalPages"));
        model.addAttribute("totalItems", result.get("totalItems"));
        model.addAttribute("headCols", headCols);

        return "company";
    }
}
