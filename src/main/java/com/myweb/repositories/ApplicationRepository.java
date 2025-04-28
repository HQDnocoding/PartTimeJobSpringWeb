/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.repositories;

import com.myweb.pojo.Application;
import java.util.Map;

/**
 *
 * @author huaquangdat
 */
public interface ApplicationRepository {
    Application addOrUpdateApplication(Application a);
    Map<String, Object> getListApplication(Map<String, String> params);
    Application getApplicationById(int applicationId);
}
