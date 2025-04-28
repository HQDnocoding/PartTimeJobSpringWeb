/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myweb.dto;

import java.math.BigInteger;
import java.util.Date;
/**
 *
 * @author Admin
 */
public class JobDTO {

    private Integer id;
    private String jobName;
    private String description;
    private String jobRequired;
    private BigInteger salaryMin;
    private BigInteger salaryMax;
    private String fullAddress;
    private String city;
    private String district;
    private String status;
    private Boolean isActive;
    private Date postedDate;
    private String companyName;
    private String majorName;
    private String dayName;

    public JobDTO(Integer id, String jobName, String description, String jobRequired, 
                  BigInteger salaryMin, BigInteger salaryMax, String fullAddress, 
                  String city, String district, String status, Boolean isActive, 
                  Date postedDate, String companyName, String majorName, String dayName) {
        this.id = id;
        this.jobName = jobName;
        this.description = description;
        this.jobRequired = jobRequired;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.fullAddress = fullAddress;
        this.city = city;
        this.district = district;
        this.status = status;
        this.isActive = isActive;
        this.postedDate = postedDate;
        this.companyName = companyName;
        this.majorName = majorName;
        this.dayName = dayName;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the jobName
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * @param jobName the jobName to set
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the jobRequired
     */
    public String getJobRequired() {
        return jobRequired;
    }

    /**
     * @param jobRequired the jobRequired to set
     */
    public void setJobRequired(String jobRequired) {
        this.jobRequired = jobRequired;
    }

    /**
     * @return the salaryMin
     */
    public BigInteger getSalaryMin() {
        return salaryMin;
    }

    /**
     * @param salaryMin the salaryMin to set
     */
    public void setSalaryMin(BigInteger salaryMin) {
        this.salaryMin = salaryMin;
    }

    /**
     * @return the salaryMax
     */
    public BigInteger getSalaryMax() {
        return salaryMax;
    }

    /**
     * @param salaryMax the salaryMax to set
     */
    public void setSalaryMax(BigInteger salaryMax) {
        this.salaryMax = salaryMax;
    }

    /**
     * @return the fullAddress
     */
    public String getFullAddress() {
        return fullAddress;
    }

    /**
     * @param fullAddress the fullAddress to set
     */
    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the district
     */
    public String getDistrict() {
        return district;
    }

    /**
     * @param district the district to set
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the isActive
     */
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return the postedDate
     */
    public Date getPostedDate() {
        return postedDate;
    }

    /**
     * @param postedDate the postedDate to set
     */
    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the majorName
     */
    public String getMajorName() {
        return majorName;
    }

    /**
     * @param majorName the majorName to set
     */
    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    /**
     * @return the dayName
     */
    public String getDayName() {
        return dayName;
    }

    /**
     * @param dayName the dayName to set
     */
    public void setDayName(String dayName) {
        this.dayName = dayName;
    }
}