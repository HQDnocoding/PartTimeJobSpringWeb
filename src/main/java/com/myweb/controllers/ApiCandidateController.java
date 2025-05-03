/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.controllers;

import com.myweb.dto.GetCandidateDTO;
import com.myweb.pojo.Candidate;
import com.myweb.services.CandidateService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
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
}
