/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
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

    public static final int PAGE_SIZE = 15;
    public static final String PAGE = "1";

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
    };

    public static enum Role {
        ROLE_ADMIN("Quản trị viên"),
        ROLE_COMPANY("Công ty"),
        ROLE_CANDIDATE("Ứng viên");
        private final String label;

        Role(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public static String dateFormatter(String input, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDate.parse(input, formatter).toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        return sdf.format(date);
    }


    public static String uploadFileToCloud(Cloudinary cloudinary, MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi upload lên Cloudinary", e);
        }
    }
}
