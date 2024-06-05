package com.diplom.web_service_attendance.controller;

import com.diplom.web_service_attendance.dto.NewReportStudyGroupAndDate;
import com.diplom.web_service_attendance.dto.ResponsReportStudyGroupAndDate;
import com.diplom.web_service_attendance.entity.StudyGroup;
import com.diplom.web_service_attendance.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;
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

//    @GetMapping("/attendance")
//    public String getAttendanceReport(@RequestParam(required = false) LocalDate startDate,
//                                      @RequestParam(required = false) LocalDate endDate,
//                                      Principal principal,
//                                      Model model) {
//        if (startDate == null) {
//            startDate = LocalDate.of(2024, 4, 1);  // LocalDate.now().minusMonths(1).plusDays(1);  todo заменить на закоменченое
//            endDate = LocalDate.now();
//        }
//
//        long studyGroupId = reportService.getStudyGroupIdByUserName(principal.getName());
//
//        List<Object[]> report = reportService.getAttendanceReport(startDate, endDate, studyGroupId);
//        List<String> reportHeaders = Arrays.asList("Фамилия", "Имя", "Отчество", "Дата рождения", "Всего пропусков", "Пропуски по уважительной причине", "Всего пар");
//
//        model.addAttribute("reportHeaders", reportHeaders);
//        model.addAttribute("report", report);
//        return "attendanceReport";
//    }

    @GetMapping("/attendance")
    public String getAttendanceReport(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                      @RequestParam(required = false) Long studyGroupId,
                                      Principal principal,
                                      Model model) {
        StudyGroup studyGroup = reportService.getStudyGroupIdByUserName(principal.getName());

        if (startDate == null) {
            startDate = LocalDate.of(2024, 4, 1);  // LocalDate.now().minusMonths(1).plusDays(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        if (studyGroupId == null) {
            studyGroupId = studyGroup.getId();
        }

        List<Object[]> report = reportService.getAttendanceReport(startDate, endDate, studyGroupId);

        List<String> reportHeaders = Arrays.asList("Фамилия", "Имя", "Отчество", "Всего пропусков", "Пропуски по уважительной причине");

        model.addAttribute("reportHeaders", reportHeaders);
        model.addAttribute("report", report);
        model.addAttribute("groupName", studyGroup.getShortName());
        model.addAttribute("totalLessons", report.getFirst()[8].toString());
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "attendanceReport";
    }














//    @PostMapping("/attendance")
//    public String setAttendanceReport(@RequestParam String startDate,
//                                      @RequestParam String endDate,
//                                      @RequestParam Long studyGroupId,
//                                      Model model) {
//        List<Object[]> report = reportService.getAttendanceReport(startDate, endDate, studyGroupId);
//        model.addAttribute("report", report);
//        return "attendanceReport";
//    }
//    @GetMapping("/attendance")
//    public String getReportAttendance(Model model,
//                                      Principal principal) {
//
//        NewReportStudyGroupAndDate report = reportService.getNewReportStudyGroupAndDate();
//
//        model.addAttribute("report", report);
//        return "report.attendance.monitor";
//    }
//
//    @PostMapping("/attendance")
//    public String getReportAttendance(@ModelAttribute("report") ResponsReportStudyGroupAndDate report,
//                                      Principal principal) {
//
//        reportService.getReportAttendance(principal.getName());
//
//        return "report.attendance.monitor";
//    }

}
