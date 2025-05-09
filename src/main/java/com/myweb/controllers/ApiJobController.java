package com.myweb.controllers;

import com.myweb.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class ApiJobController { // Đã đổi tên từ JobApiController thành ApiJobController

    @Autowired
    private JobService jobService;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable("id") int id) {
        try {
            jobService.deleteJob(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Đã xảy ra lỗi khi xóa công việc: " + e.getMessage());
        }
    }
}