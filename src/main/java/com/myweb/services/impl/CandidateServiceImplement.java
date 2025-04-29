/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.services.impl;

import com.myweb.services.CandidateService;
import com.myweb.pojo.Candidate;
import com.myweb.repositories.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author huaquangdat
 */
@Service
public class CandidateServiceImplement implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    public Candidate addOrUpdateCandidate(Candidate c) {
        return candidateRepository.addOrUpdateCandidate(c);
    }

    public Map<String, Object> getListCandidate(Map<String, String> params) {
        return candidateRepository.getListCandidate(params);
    }

    public Candidate getCandidateById(int candidateId) {
        return candidateRepository.getCandidateById(candidateId);
    }

}
