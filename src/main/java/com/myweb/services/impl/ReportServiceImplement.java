package com.myweb.services.impl;

import com.myweb.dto.ReportDTO;
import com.myweb.repositories.ReportRepository;
import com.myweb.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImplement implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public List<ReportDTO> generateReport(Date fromDate, Date toDate) {
        List<Object[]> jobData = reportRepository.generateJobReport(fromDate, toDate);
        List<Object[]> candidateData = reportRepository.generateCandidateReport(fromDate, toDate);
        List<Object[]> companyData = reportRepository.generateCompanyReport(fromDate, toDate);

        Map<Date, ReportDTO> reportMap = new HashMap<>();

        for (Object[] row : jobData) {
            Date date = (Date) row[0];
            Long jobCount = ((Number) row[1]).longValue();
            reportMap.computeIfAbsent(date, k -> new ReportDTO(date, 0L, 0L, 0L))
                     .setJobCount(jobCount);
        }

        for (Object[] row : candidateData) {
            Date date = (Date) row[0];
            Long candidateCount = ((Number) row[1]).longValue();
            reportMap.computeIfAbsent(date, k -> new ReportDTO(date, 0L, 0L, 0L))
                     .setCandidateCount(candidateCount);
        }

        for (Object[] row : companyData) {
            Date date = (Date) row[0];
            Long companyCount = ((Number) row[1]).longValue();
            reportMap.computeIfAbsent(date, k -> new ReportDTO(date, 0L, 0L, 0L))
                     .setCompanyCount(companyCount);
        }

        return reportMap.values().stream()
                       .sorted(Comparator.comparing(ReportDTO::getDate))
                       .collect(Collectors.toList());
    }
}