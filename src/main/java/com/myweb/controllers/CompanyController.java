/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.controllers;

import com.myweb.dto.CreateCompanyDTO;
import com.myweb.pojo.Company;
import com.myweb.services.CompanyService;
import com.myweb.utils.GeneralUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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



    @GetMapping("/companies")
    public String companyView(Model model, @RequestParam Map<String, String> params) {
        Collection<String> headCols = new ArrayList<>(List.of("Id", "Tên công ty", "Email", "Địa chỉ", "Mã số thuế", "Ngày đăng ký", "Trạng thái"));

        Map<String, Object> result = cpnyService.getListCompany(params);

        model.addAttribute("companies", result.get("companies"));
        model.addAttribute("currentPage", result.get("currentPage"));
        model.addAttribute("pageSize", result.get("pageSize"));
        model.addAttribute("totalPages", result.get("totalPages"));
        model.addAttribute("totalItems", result.get("totalItems"));
        model.addAttribute("headCols", headCols);

        return "company";
    }

    @GetMapping("/companies/{companyId}")
    public String companyDetailView(Model model, @PathVariable(value = "companyId") int id) {
        model.addAttribute("company", this.cpnyService.getCompany(id));
        return "company-detail";
    }

    @PostMapping("/companies/{companyId}/update-status")
    public String updateCompanyStatus(@PathVariable("companyId") int id, @RequestParam("status") String status) {
        Company c = this.cpnyService.getCompany(id);
        c.setStatus(status);
        this.cpnyService.addOrUpdate(c);
        return "redirect:/companies/" + id;
    }

    @GetMapping("/companies/create-company")
    public String createCompanyView(Model model) {
        model.addAttribute("companyDTO", new CreateCompanyDTO());
        return "create-company";
    }

    @PostMapping("/companies")
    public String createCompany(Model model, @ModelAttribute(value = "companyDTO") CreateCompanyDTO companyDTO) {
        try {
            Company company = this.cpnyService.createCompanyDTO(companyDTO);
            return "redirect:/companies/" + company.getId();
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "create-company";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Đã có lỗi xảy ra, vui lòng thử lại.");
            return "create-company";
        }
    }

}
