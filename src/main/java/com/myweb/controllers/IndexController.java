/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Hau
 */

//Khi người dùng truy cập (URL gốc), Spring sẽ gọi phương thức index trong IndexController
//Phương thức này trả về chuỗi "index", sau đó vào ThymeleafConfig tìm nơi chứa 
//Spring sẽ tìm một tệp View (index.html) để hiển thị cho người dùng.
@Controller
@ControllerAdvice
public class IndexController {
    
    @RequestMapping("/")
    public String index(Model model){
        return  "index";
    }
}
