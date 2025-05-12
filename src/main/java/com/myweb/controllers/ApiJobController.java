package com.myweb.controllers;

import com.myweb.dto.CreateJobDTO;
import com.myweb.services.JobService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createJob(CreateJobDTO dto) {
        System.out.println("go" +dto.getLatitude());

        try {
            Object result = this.jobService.createJobDTO(dto);
            System.out.println(dto);
            return new ResponseEntity<>(Map.of("message", "Tạo bài tuyển dụng thành công", "data", result), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", String.format("Tạo thất bại: %s, %s", dto.getLongitude(), e.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
