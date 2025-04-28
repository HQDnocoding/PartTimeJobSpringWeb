/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.controllers;

import com.myweb.services.DayService;
import com.myweb.services.JobService;
import com.myweb.services.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private MajorService majorService;

    @Autowired
    private DayService dayService;

    @GetMapping
    public String listJobs(Model model) {
        model.addAttribute("jobs", jobService.getAllJobs());
        model.addAttribute("majors", majorService.getMajors());
        model.addAttribute("days", dayService.getDays());
        return "job-list";
    }

    @GetMapping("/{id}")
    public String jobDetail(@PathVariable("id") int id, Model model) {
        model.addAttribute("job", jobService.getDetailJobById(id));
        model.addAttribute("majors", majorService.getMajors());
        model.addAttribute("days", dayService.getDays());
        return "job-detail";
    }
}