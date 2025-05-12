package com.myweb.controllers;

import com.myweb.dto.ReportDTO;
import com.myweb.services.ReportService;
import java.text.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private SimpleDateFormat simpleDateFormat;

    @GetMapping
    public String report(Model model,
                        @RequestParam(name = "fromDate", defaultValue = "") String fromDate,
                        @RequestParam(name = "toDate", defaultValue = "") String toDate) throws ParseException {
        Date fr = null, to = null;
        if (!fromDate.isEmpty()) {
            fr = simpleDateFormat.parse(fromDate);
        }
        if (!toDate.isEmpty()) {
            to = simpleDateFormat.parse(toDate);
        }

        List<ReportDTO> reports = reportService.generateReport(fr, to);
        System.out.println("Reports size: " + reports.size());
        reports.forEach(report -> System.out.println("Report: " + report.getDate() + ", Jobs: " + report.getJobCount() + ", Candidates: " + report.getCandidateCount() + ", Companies: " + report.getCompanyCount()));
        model.addAttribute("reportData", reports);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        return "report";
    }

    @GetMapping("/data")
    @ResponseBody
    public List<ReportDTO> getReportData(
            @RequestParam(name = "fromDate", required = false) String fromDate,
            @RequestParam(name = "toDate", required = false) String toDate) throws ParseException {
        Date fr = null, to = null;
        if (fromDate != null && !fromDate.isEmpty()) {
            fr = simpleDateFormat.parse(fromDate);
        }
        if (toDate != null && !toDate.isEmpty()) {
            to = simpleDateFormat.parse(toDate);
        }
        List<ReportDTO> reports = reportService.generateReport(fr, to);
        System.out.println("Reports size (JSON): " + reports.size());
        return reports;
    }
}