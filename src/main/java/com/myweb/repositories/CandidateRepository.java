package com.myweb.repositories;

import com.myweb.pojo.Candidate;
import java.util.Map;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author huaquangdat
 */
public interface CandidateRepository {

    Candidate addOrUpdateCandidate(Candidate c);

    Map<String, Object> getListCandidate(Map<String, String> params);

    Candidate getCandidateById(int candidateId);
}
