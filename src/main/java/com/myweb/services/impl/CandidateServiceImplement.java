/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.services.impl;

import com.cloudinary.Cloudinary;
import com.myweb.dto.CreateCandidateDTO;
import com.myweb.services.CandidateService;
import com.myweb.pojo.Candidate;
import com.myweb.pojo.User;
import com.myweb.repositories.CandidateRepository;
import com.myweb.repositories.UserRepository;
import com.myweb.utils.GeneralUtils;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.myweb.utils.ValidationUtils.isValidPassword;
import static com.myweb.utils.ValidationUtils.isValidUsername;

/**
 *
 * @author huaquangdat
 */
@Service
public class CandidateServiceImplement implements CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Thêm hoặc cập nhật ứng viên
    @Override
    public Candidate addOrUpdateCandidate(Candidate c) {
        if (c.getId() != null) {
            if (!c.getAvatarFile().isEmpty()) {
                c.setCurriculumVitae(GeneralUtils.uploadFileToCloud(cloudinary, c.getCurriculumVitaeFile()));
            }
            if (!c.getAvatarFile().isEmpty()) {
                c.setAvatar(GeneralUtils.uploadFileToCloud(cloudinary, c.getAvatarFile()));
            }
        }
        return this.candidateRepository.addOrUpdateCandidate(c);
    }

    // Lấy danh sách ứng viên với lọc/phân trang
    @Override
    public Map<String, Object> getListCandidate(Map<String, String> params) {
        return this.candidateRepository.getListCandidate(params);
    }

    // Lấy chi tiết ứng viên theo ID
    @Override
    public Candidate getCandidateById(int candidateId) {
        return this.candidateRepository.getCandidateById(candidateId);
    }

    // Tạo 1 ứng viên mới
    @Override
    public Candidate createCandidateDTO(CreateCandidateDTO c) {
        // Kiểm tra các trường bắt buộc không được null hoặc rỗng
        if (c.getUsername() == null || c.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập tên đăng nhập.");
        }
        if (userRepository.getUserByUsername(c.getUsername()) != null) {
            throw new IllegalArgumentException("Tên đăng nhập đã được sử dụng.");
        }
        if (!isValidPassword(c.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ thường, chữ hoa và số.");
        }
        if (!isValidUsername(c.getUsername())) {
            throw new IllegalArgumentException("Tên đăng nhập phải trên 8 ký tự, bắt đầu bằng chữ cái và không chứa khoảng trắng.");
        }

        if (c.getPassword() == null || c.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập mật khẩu.");
        }

        if (c.getFullName() == null || c.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập họ và tên.");
        }

        if (c.getEmail() == null || c.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập email.");
        }
        if (candidateRepository.getCandidateByEmail(c.getEmail()) != null) {
            throw new IllegalArgumentException("Email đã được sử dụng.");
        }

        if (c.getPhone() == null || c.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập số điện thoại.");
        }
        if (candidateRepository.getCandidateByPhone(c.getPhone()) != null) {
            throw new IllegalArgumentException("Số điện thoại này đã được sử dụng.");
        }

        if (c.getCity() == null || c.getCity().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn thành phố.");
        }

        if (c.getDateOfBirth() == null) {
            throw new IllegalArgumentException("Vui lòng chọn ngày sinh.");
        }

        if (c.getAvatarFile() == null || c.getAvatarFile().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn hình đại diện.");
        }

        // Tạo tài khoản người dùng
        User u = new User();
        u.setUsername(c.getUsername());
        u.setPassword(this.passwordEncoder.encode(c.getPassword()));
        u.setRegisterDate(new Date());
        u.setRole(GeneralUtils.Role.ROLE_CANDIDATE.toString());
        u.setIsActive(true);

        // Tạo ứng viên
        Candidate can = new Candidate();
        can.setCity(c.getCity());
        can.setFullName(c.getFullName());
        can.setAvatar(GeneralUtils.uploadFileToCloud(cloudinary, c.getAvatarFile()));
        can.setEmail(c.getEmail());
        can.setDateOfBirth(c.getDateOfBirth());
        can.setPhone(c.getPhone());
        can.setSelfDescription(c.getSelfDescription());

        if (c.getCurriculumVitaeFile() != null && !c.getCurriculumVitaeFile().isEmpty()) {
            can.setCurriculumVitae(GeneralUtils.uploadFileToCloud(cloudinary, c.getCurriculumVitaeFile()));
        }

        can.setUserId(u);

        try {
            return this.candidateRepository.createCandidate(u, can);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Dữ liệu không hợp lệ, vui lòng kiểm tra lại thông tin.");
        }
    }

    // Lấy tất cả ứng viên
    @Override
    public List<Candidate> getCandidateList() {
        return this.candidateRepository.getCandidateList();
    }

    // Xóa ứng viên theo ID, xử lý ngoại lệ
    @Override
    public void deleteCandidate(int id) {
        try {
            this.candidateRepository.deleteCandidate(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Candidate getCandidateByUserId(int userId) {
        
        return this.candidateRepository.getCandidateByUserId(userId);
    }
    
    
    

}
