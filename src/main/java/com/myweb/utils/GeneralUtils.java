/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dat
 */
public class GeneralUtils {

    public static enum Status {
        pending("Chờ xử lý"),
        approved("Đã duyệt"),
        refused("Bị từ chối");
        private final String label;

        Status(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }


        
        
    }

    public static LocalDate dateFormatter(String input, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDate.parse(input, formatter);
        } catch (Exception e) {
            return null;
        }
    }
}
