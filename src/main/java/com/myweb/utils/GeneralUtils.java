/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.Charsets;

/**
 *
 * @author dat
 */
public class GeneralUtils {

    public static final int PAGE_SIZE = 6;
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

    private static final HttpClient client = HttpClient.newHttpClient();
    public static String getProvince() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://provinces.open-api.vn/api/p/"))
                .header("Accept", "application/json; charset=utf-8")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDistrict(Integer cityId) {
        String url   = String.format("https://provinces.open-api.vn/api/p/%d/", cityId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json; charset=utf-8")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body(); 
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

}
