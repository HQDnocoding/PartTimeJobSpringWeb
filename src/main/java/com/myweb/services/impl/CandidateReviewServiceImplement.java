/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.services.impl;

import com.myweb.dto.CreateCandidateReviewDTO;
import com.myweb.dto.GetCandidateReviewDTO;
import com.myweb.pojo.Application;
import com.myweb.pojo.CandidateReview;
import com.myweb.pojo.Company;
import com.myweb.pojo.Job;
import com.myweb.pojo.User;
import com.myweb.repositories.ApplicationRepository;
import com.myweb.repositories.CandidateReviewRepository;
import com.myweb.repositories.CompanyRepository;
import com.myweb.repositories.JobRepository;
import com.myweb.repositories.UserRepository;
import com.myweb.services.CandidateReviewService;
import com.myweb.utils.GeneralUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CandidateReviewServiceImplement implements CandidateReviewService {

    @Autowired
    private CandidateReviewRepository reviewRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public GetCandidateReviewDTO createReview(CreateCandidateReviewDTO dto, Principal principal) {
        User user = userRepository.getUserByUsername(principal.getName());
        if (!user.getRole().equals(GeneralUtils.Role.ROLE_COMPANY.toString())) {
            throw new SecurityException("Only companies can create candidate reviews.");
        }

        Company company = companyRepository.getCompanyById(dto.getCompanyId());
        Job job = jobRepository.getJobById(dto.getJobId());

        if (company == null || job == null) {
            throw new IllegalArgumentException("Invalid company or job.");
        }

        if (!company.getUserId().getId().equals(user.getId())) {
            throw new SecurityException("You are not authorized to create this review.");
        }

        if (!job.getCompanyId().getId().equals(company.getId())) {
            throw new IllegalArgumentException("Job does not belong to this company.");
        }

        // Kiểm tra xem có Application liên quan với trạng thái approved
        List<Application> applications = applicationRepository.findByJobIdAndStatus(
            dto.getJobId(), GeneralUtils.Status.approved.toString()
        );
        if (applications.isEmpty()) {
            throw new IllegalArgumentException("No approved application found for this job.");
        }

        if (dto.getRating() < 1 || dto.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        CandidateReview review = new CandidateReview();
        review.setCompanyId(company);
        review.setJobId(job);
        review.setRating(dto.getRating());
        review.setReview(dto.getReview());
        review.setReviewDate(new Date());

        review = reviewRepository.addOrUpdateReview(review);
        return new GetCandidateReviewDTO(review);
    }

    @Override
    public Map<String, Object> getListReview(Map<String, String> params, Integer candidateId) {
        return reviewRepository.getListReview(params, candidateId);
    }

    @Override
    public GetCandidateReviewDTO getReviewById(Integer id) {
        CandidateReview review = reviewRepository.getReviewById(id);
        if (review == null) {
            throw new IllegalArgumentException("Review not found.");
        }
        return new GetCandidateReviewDTO(review);
    }

    @Override
    @Transactional
    public GetCandidateReviewDTO updateReview(Integer id, CreateCandidateReviewDTO dto, Principal principal) {
        User user = userRepository.getUserByUsername(principal.getName());
        CandidateReview review = reviewRepository.getReviewById(id);

        if (review == null) {
            throw new IllegalArgumentException("Review not found.");
        }

        if (!user.getRole().equals(GeneralUtils.Role.ROLE_COMPANY.toString()) ||
            !review.getCompanyId().getUserId().getId().equals(user.getId())) {
            throw new SecurityException("You are not authorized to update this review.");
        }

        if (dto.getRating() < 1 || dto.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        review.setRating(dto.getRating());
        review.setReview(dto.getReview());
        review = reviewRepository.addOrUpdateReview(review);
        return new GetCandidateReviewDTO(review);
    }

    @Override
    @Transactional
    public void deleteReview(Integer id, Principal principal) {
        User user = userRepository.getUserByUsername(principal.getName());
        CandidateReview review = reviewRepository.getReviewById(id);

        if (review == null) {
            throw new IllegalArgumentException("Review not found.");
        }

        if (!user.getRole().equals(GeneralUtils.Role.ROLE_COMPANY.toString()) &&
            !user.getRole().equals(GeneralUtils.Role.ROLE_ADMIN.toString()) ||
            (!user.getRole().equals(GeneralUtils.Role.ROLE_ADMIN.toString()) &&
             !review.getCompanyId().getUserId().getId().equals(user.getId()))) {
            throw new SecurityException("You are not authorized to delete this review.");
        }

        reviewRepository.deleteReview(id);
    }

    @Override
    public Double getAverageRating(Integer candidateId) {
        return reviewRepository.getAverageRating(candidateId);
    }
}
