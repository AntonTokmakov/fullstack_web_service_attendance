package com.diplom.web_service_attendance.controller;

import com.diplom.web_service_attendance.dto.NewReportStudyGroupAndDate;
import com.diplom.web_service_attendance.dto.ResponsReportStudyGroupAndDate;
import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.entity.securityEntity.WebUser;
import com.diplom.web_service_attendance.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/app/report")
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public String getReport() {
        return "report";
    }

    @GetMapping("/attendance")
    public String getReportAttendance(Model model,
                                      Principal principal) {

        NewReportStudyGroupAndDate report = reportService.getNewReportStudyGroupAndDate();

        model.addAttribute("report", report);
        return "report.attendance";
    }

    @PostMapping("/attendance")
    public String getReportAttendance(@ModelAttribute("report") ResponsReportStudyGroupAndDate report,
                                      Principal principal) {

        reportService.getReportAttendance(principal.getName());

        return "report.attendance";
    }

}
