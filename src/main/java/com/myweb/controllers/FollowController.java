/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.controllers;

import com.myweb.pojo.Follow;
import com.myweb.pojo.User;
import com.myweb.services.FollowService;
import com.myweb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @PostMapping("/{companyId}")
    public ResponseEntity<Follow> followCompany(@PathVariable("companyId") int companyId, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        if (user == null || !user.getRole().equals("ROLE_CANDIDATE")) {
            return ResponseEntity.status(403).build();
        }
        int candidateId = user.getCandidate().getId(); // Giả định User có liên kết với Candidate
        Follow follow = followService.followCompany(candidateId, companyId);
        return ResponseEntity.ok(follow);
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> unfollowCompany(@PathVariable("companyId") int companyId, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        if (user == null || !user.getRole().equals("ROLE_CANDIDATE")) {
            return ResponseEntity.status(403).build();
        }
        int candidateId = user.getCandidate().getId();
        followService.unfollowCompany(candidateId, companyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/is-following/{companyId}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable("companyId") int companyId, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        if (user == null || !user.getRole().equals("ROLE_CANDIDATE")) {
            return ResponseEntity.status(403).build();
        }
        int candidateId = user.getCandidate().getId();
        boolean isFollowing = followService.isFollowing(candidateId, companyId);
        return ResponseEntity.ok(isFollowing);
    }

    @GetMapping("/followed-companies")
    public ResponseEntity<List<Follow>> getFollowedCompanies(Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        if (user == null || !user.getRole().equals("ROLE_CANDIDATE")) {
            return ResponseEntity.status(403).build();
        }
        int candidateId = user.getCandidate().getId();
        List<Follow> follows = followService.getFollowedCompanies(candidateId);
        return ResponseEntity.ok(follows);
    }
}