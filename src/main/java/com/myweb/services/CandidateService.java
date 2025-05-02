/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.services;

import com.myweb.dto.CreateCandidateDTO;
import com.myweb.pojo.Candidate;
import java.util.List;
import java.util.Map;

/**
 *
 * @author huaquangdat
 */
public interface CandidateService {

    Candidate addOrUpdateCandidate(Candidate c);

    Map<String, Object> getListCandidate(Map<String, String> params);

    Candidate getCandidateById(int candidateId);

    Candidate createCandidateDTO(CreateCandidateDTO c);

    
    List<Candidate> getCandidateList();
    
    void deleteCandidate(int id);
}
