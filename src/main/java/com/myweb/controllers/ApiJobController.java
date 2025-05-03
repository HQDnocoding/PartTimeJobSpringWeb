/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.controllers;

import com.myweb.dto.GetJobDTO;
import com.myweb.pojo.Job;
import com.myweb.services.JobService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author huaquangdat
 */
@RestController
@RequestMapping("/api")
public class ApiJobController {

    @Autowired
    private JobService jobService;

//    @GetMapping("/jobs")
//    public List<GetJobDTO> getAllJob() {
//        List<Job> list = this.jobService.getJobList();
//        return list.stream().map(j -> {
//            GetJobDTO dto = new GetJobDTO(j);
//            return dto;
//        }
//        ).collect(Collectors.toList());
//    }
}
