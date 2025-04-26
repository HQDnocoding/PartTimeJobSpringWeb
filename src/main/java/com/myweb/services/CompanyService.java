/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.services;

import com.myweb.pojo.Company;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author dat
 */
@Service
public interface CompanyService {

    List<Object[]> getListCompany(int page);
}
