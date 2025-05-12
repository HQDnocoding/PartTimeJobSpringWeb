package com.myweb.services;

import com.myweb.dto.ReportDTO;
import java.util.Date;
import java.util.List;

public interface ReportService {
    List<ReportDTO> generateReport(Date fromDate, Date toDate);
}