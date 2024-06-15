package com.diplom.web_service_attendance.controller;

import com.diplom.web_service_attendance.dto.NewReportStudyGroupAndDate;
import com.diplom.web_service_attendance.dto.ResponsReportStudyGroupAndDate;
import com.diplom.web_service_attendance.entity.Student;
import com.diplom.web_service_attendance.entity.StudyGroup;
import com.diplom.web_service_attendance.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.diplom.web_service_attendance.service.ReportService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/app/report")
public class ReportController {

    private final ReportService reportService;
    private StudyGroup studyGroupByUserName;

    @GetMapping
    public String getListReport() {
        return "report";
    }

    @GetMapping("/attendance")
    public String getAttendanceReport(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                      @RequestParam(required = false) Long studyGroupId,
                                      Principal principal,
                                      Model model) {
        studyGroupByUserName = reportService.getStudyGroupIdByUserName(principal.getName());

        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(1).plusDays(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        if (studyGroupId == null) {
            studyGroupId = studyGroupByUserName.getId();
        }

        List<Object[]> report = reportService.getAttendanceReport(startDate, endDate, studyGroupId);
        List<String> reportHeaders = Arrays.asList("Фамилия", "Имя", "Отчество", "Всего пропусков", "Пропуски по уважительной причине");

        int totalMissedLessons = 0;
        int totalRespectMissedStatus = 0;

        for (Object[] row : report) {
            totalMissedLessons += ((Number) row[6]).intValue();
            totalRespectMissedStatus += ((Number) row[7]).intValue();
        }

        model.addAttribute("reportHeaders", reportHeaders);
        model.addAttribute("report", report);
        model.addAttribute("groupName", studyGroupByUserName.getShortName());
        model.addAttribute("totalLessons", report.getFirst()[8].toString());
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("totalMissedLessons", totalMissedLessons);
        model.addAttribute("totalRespectMissedStatus", totalRespectMissedStatus);
        model.addAttribute("monitor", reportService.getMonitorName(studyGroupByUserName));
        return "attendanceReport";
    }

    @GetMapping("/attendance/download")
    public ResponseEntity<InputStreamResource> downloadAttendanceReport(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                                        @RequestParam(required = false) Long studyGroupId) throws IOException {
        // Получение данных для отчета
        if (startDate == null) {
            startDate = LocalDate.of(2024, 4, 1);  // Можно заменить на LocalDate.now().minusMonths(1).plusDays(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        if (studyGroupId == null) {
            studyGroupId = studyGroupByUserName.getId();
        }

        List<Object[]> report = reportService.getAttendanceReport(startDate, endDate, studyGroupId);

        // Создание отчета в формате Excel
        ByteArrayInputStream in = reportService.createExcelReport(report, startDate, endDate, studyGroupByUserName);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=attendance_report.xlsx");

        // Возвращение файла
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(in));
    }


    @GetMapping("/semester-attendance")
    public String getSemesterAttendanceReport(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                              @RequestParam(required = false) Long studyGroupId,
                                              Principal principal,
                                              Model model) {
        studyGroupByUserName = reportService.getStudyGroupIdByUserName(principal.getName());

        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(6).plusDays(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        if (studyGroupId == null) {
            studyGroupId = studyGroupByUserName.getId();
        }

        List<Object[]> report = reportService.getSemesterAttendanceReport(startDate, endDate, studyGroupId);

        Map<String, Map<Integer, int[]>> reportData = new LinkedHashMap<>();
        for (Object[] row : report) {
            String student = row[0] + " " + row[1] + " " + row[2];
            int year = ((Number) row[3]).intValue();
            int month = ((Number) row[4]).intValue();
            int totalMissedLessons = ((Number) row[5]).intValue();
            int respectMissedStatus = ((Number) row[6]).intValue();

            int monthKey = year * 100 + month;

            reportData.putIfAbsent(student, new LinkedHashMap<>());
            reportData.get(student).put(monthKey, new int[]{totalMissedLessons, respectMissedStatus});
        }

        model.addAttribute("reportData", reportData);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("months", getMonthsBetweenDates(startDate, endDate));

        return "semesterAttendanceReport";
    }

    private List<LocalDate> getMonthsBetweenDates(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> months = new ArrayList<>();
        LocalDate current = startDate.withDayOfMonth(1);
        while (!current.isAfter(endDate.withDayOfMonth(1))) {
            months.add(current);
            current = current.plusMonths(1);
        }
        return months;
    }

}
