/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.controllers;

import com.myweb.dto.CreateCandidateDTO;
import com.myweb.dto.GetCandidateDTO;
import com.myweb.pojo.Candidate;
import com.myweb.services.CandidateService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huaquangdat
 */
@RequestMapping("/api")
@RestController
public class ApiCandidateController {

    @Autowired
    private CandidateService candidateService;

    @GetMapping("/candidates")
    public List<GetCandidateDTO> getCandidateList() {
        List<Candidate> candidates = candidateService.getCandidateList();
        return candidates.stream().map(c -> {
            GetCandidateDTO dto = new GetCandidateDTO(c);
            return dto;
        }).collect(Collectors.toList());
    }

    @DeleteMapping("/candidates/{candidateId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable(value = "candidateId") int candidateId) {
        try {
            this.candidateService.deleteCandidate(candidateId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping(path = "/register-candidate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCandidate(CreateCandidateDTO dto) {
        try {
            Object result = this.candidateService.createCandidateDTO(dto);
            return new ResponseEntity<>(Map.of("message", "Đăng ký thành công", "data", result), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("message", "Đăng ký thất bại: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
