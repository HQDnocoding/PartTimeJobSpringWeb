/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.controllers;

import com.myweb.pojo.Company;
import com.myweb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author huaquangdat
 */
@Controller
public class UserController {
    
    @Autowired
    private UserService userService; 
    
    
    @GetMapping("/login")
    public String loginView(){
        return "login";
    }
    
    @GetMapping("/register")
    public String registerView(Model model){
        model.addAttribute("company" ,new Company());
        return "register";
    }
    
//    @PostMapping
//    public String regist(@ModelAttribute(value="candidate") Company company){
//        this.userService.addUser(params, avatar)
//    }
}
