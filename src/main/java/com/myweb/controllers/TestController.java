package com.myweb.controllers;

import com.myweb.dto.CreateJobDTO;
import com.myweb.dto.GetJobDTO;
import com.myweb.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private JobService jobService;

    @PostMapping("/create-job")
    public ResponseEntity<GetJobDTO> testCreateJob(@RequestBody CreateJobDTO jobDTO) {
        GetJobDTO result = jobService.createJobDTO(jobDTO);
        return ResponseEntity.status(201).body(result);
    }
}
