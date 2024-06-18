package com.diplom.web_service_attendance.controller;

import com.diplom.web_service_attendance.Excaption.NotFountStudyGroup;
import com.diplom.web_service_attendance.dto.CheckActualLesson;
import com.diplom.web_service_attendance.dto.SetPassActualLessonGroupStudy;
import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.entity.Lesson;
import com.diplom.web_service_attendance.entity.StudyGroup;
import com.diplom.web_service_attendance.repository.CheckingPassRepository;
import com.diplom.web_service_attendance.repository.LessonRepository;
import com.diplom.web_service_attendance.repository.TeacherRepository;
import com.diplom.web_service_attendance.service.PassService;
import com.diplom.web_service_attendance.service.ReportService;
import com.diplom.web_service_attendance.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/app/teacher")
public class TeacherController {

    private final TeacherService teacherService;
    private final PassService passService;

    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;

    @GetMapping("/report")
    public String getListReport() {
        return "report";
    }

    @PreAuthorize("hasAuthority('TEACHER')")
    @GetMapping("/groups")
    public String getGroups(@PathVariable(value = "date", required = false) LocalDate date,
                            Principal principal,
                            Model model) {

        List<CheckActualLesson> checkActualLessons = new ArrayList<>();
        if (date == null) {
            date = LocalDate.now();
        }

        try {
            List<ActualLesson> lessonList = teacherService.findActualLessonByDateAndTeacher(date, principal.getName());

            checkActualLessons = lessonList.stream()
                    .map(actualLesson -> CheckActualLesson.builder()
                            .id(actualLesson.getId())
                            .lesson(actualLesson.getLesson())
                            .date(actualLesson.getDate())
//                            .isAttendence(passRepository.existsByActualLessonId(actualLesson.getId()))
                            .isAttendence(passService.getExistsPass(actualLesson.getId()))
                            .build())
                    .toList();
        } catch (InvalidParameterException e){
            e.getStackTrace();
        }

        model.addAttribute("studyGroupList", checkActualLessons);
        return "teacher.groups";
    }

    @PreAuthorize("hasAuthority('MONITOR')")
    @GetMapping("/pass/{lessonId}")
    public String getStudentToPass(@PathVariable("lessonId") Long lessonId,
                                   Principal principal,
                                   Model model) {

        String username = principal.getName();
        SetPassActualLessonGroupStudy pass = null;
        try {
            pass = teacherService.getStudentByStudyGroupToPass(username, lessonId);
        } catch (NotFountStudyGroup e) {
            throw new RuntimeException(e);
        }

        model.addAttribute("setPassActualLessonGroupStudy", pass);
        return "lessons.teacher.pass";
    }

    @PreAuthorize("hasAuthority('MONITOR')")
    @PostMapping("/pass/{actualLessonId}")
    public String handlePassFormSubmission(@PathVariable("actualLessonId") Long actualLessonId,
                                           @RequestParam(value = "studentList", required = false, defaultValue = "") String[] passStudentId,
                                           @RequestParam(value = "editTeacher", required = false, defaultValue = "false") boolean editTeacher) {

        if (editTeacher) {
            List<Long> passStudentIdList = Arrays.stream(passStudentId).map(Long::parseLong).collect(Collectors.toList());
            passService.savePassActualLesson(actualLessonId, passStudentIdList);
            passService.setCheckPass(actualLessonId);
        } else {
            passService.setCheckPass(actualLessonId);
        }

        return "redirect:/app/teacher/groups";
    }




    ////////////// report


    private final ReportService reportService;

    private StudyGroup studyGroupByUserName;




    @GetMapping("/attendance")
    public String getAttendanceReport(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                      @RequestParam(required = false) Long studyGroupId,
                                      Principal principal,
                                      Model model) {
        List<StudyGroup> studyGroups = reportService.getStudyGroups();

        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(1).plusDays(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        if (studyGroupId == null) {
            studyGroupId = studyGroups.get(0).getId();
        } else {
            studyGroupByUserName = reportService.getStudyGroup(studyGroupId);
        }

        List<Object[]> report = reportService.getAttendanceReport(startDate, endDate, studyGroupId);
        List<String> reportHeaders = Arrays.asList("Фамилия", "Имя", "Отчество", "Всего пропусков", "Пропуски по уважительной причине");

        int totalMissedLessons = 0;
        int totalRespectMissedStatus = 0;

        for (Object[] row : report) {
            totalMissedLessons += ((Number) row[6]).intValue();
            totalRespectMissedStatus += ((Number) row[7]).intValue();
        }

        model.addAttribute("studyGroups", studyGroups);
        model.addAttribute("reportHeaders", reportHeaders);
        model.addAttribute("report", report);
        model.addAttribute("groupName", studyGroupByUserName.getShortName());
        model.addAttribute("totalLessons", report.getFirst()[8].toString());
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("totalMissedLessons", totalMissedLessons);
        model.addAttribute("totalRespectMissedStatus", totalRespectMissedStatus);
        model.addAttribute("monitor", reportService.getMonitorName(studyGroupByUserName));
        return "teacher.attendanceReport";
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
        List<StudyGroup> studyGroups = reportService.getStudyGroups();

        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(6).plusDays(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        if (studyGroupId == null) {
            studyGroupId = studyGroups.get(0).getId();
        } else {
            studyGroupByUserName = reportService.getStudyGroup(studyGroupId);
        }

        List<Object[]> report = reportService.getSemesterAttendanceReport(startDate, endDate, studyGroupId);

        Map<String, Map<Integer, int[]>> reportData = new LinkedHashMap<>();
        Map<String, int[]> totalAttendance = new LinkedHashMap<>(); // Для хранения общего количества пропусков

        int totalMissedLessonsSum = 0;
        int respectMissedStatusSum = 0;

        for (Object[] row : report) {
            String student = row[0] + " " + row[1] + " " + row[2];
            int year = ((Number) row[3]).intValue();
            int month = ((Number) row[4]).intValue();
            int totalMissedLessons = ((Number) row[5]).intValue();
            int respectMissedStatus = ((Number) row[6]).intValue();

            totalMissedLessonsSum += totalMissedLessons;
            respectMissedStatusSum += respectMissedStatus;

            int monthKey = year * 100 + month;

            reportData.putIfAbsent(student, new LinkedHashMap<>());
            reportData.get(student).put(monthKey, new int[]{totalMissedLessons, respectMissedStatus});

            totalAttendance.putIfAbsent(student, new int[]{0, 0});
            totalAttendance.get(student)[0] += totalMissedLessons;
            totalAttendance.get(student)[1] += respectMissedStatus;
        }

        model.addAttribute("studyGroups", studyGroups);
        model.addAttribute("reportData", reportData);
        model.addAttribute("monitor", reportService.getMonitorName(studyGroupByUserName));
        model.addAttribute("groupName", studyGroupByUserName.getShortName());
        model.addAttribute("totalAttendance", totalAttendance);
        model.addAttribute("totalMissedLessonsSum", totalMissedLessonsSum);
        model.addAttribute("respectMissedStatusSum", respectMissedStatusSum);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("months", getMonthsBetweenDates(startDate, endDate));

        return "teacher.semesterAttendanceReport.html";
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


    @GetMapping("/semester-attendance/download")
    public ResponseEntity<byte[]> downloadSemesterAttendanceReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Principal principal) throws IOException {

        StudyGroup studyGroupByUserName = reportService.getStudyGroupIdByUserName(principal.getName());
        List<Object[]> report = reportService.getSemesterAttendanceReport(startDate, endDate, studyGroupByUserName.getId());

        // Create Excel file
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Semester Attendance Report");

        // Create styles
        CellStyle headerStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setWrapText(true);

        CellStyle normalStyle = workbook.createCellStyle();
        normalStyle.setAlignment(HorizontalAlignment.CENTER);
        normalStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        normalStyle.setWrapText(true);

        CellStyle borderedStyle = workbook.createCellStyle();
        borderedStyle.setAlignment(HorizontalAlignment.CENTER);
        borderedStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        borderedStyle.setWrapText(true);
        borderedStyle.setBorderBottom(BorderStyle.THIN);
        borderedStyle.setBorderTop(BorderStyle.THIN);
        borderedStyle.setBorderLeft(BorderStyle.THIN);
        borderedStyle.setBorderRight(BorderStyle.THIN);

        // Title
        int rowIdx = 0;
        XSSFRow titleRow = sheet.createRow(rowIdx++);
        XSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Сводная ведомость учета посещаемости занятий студентами группы " + studyGroupByUserName.getShortName());
        titleCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));

        XSSFRow subtitleRow = sheet.createRow(rowIdx++);
        XSSFCell subtitleCell = subtitleRow.createCell(0);
        subtitleCell.setCellValue("(всего / по неуважительной причине)");
        subtitleCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));

        // Group info
        XSSFRow groupInfoRow = sheet.createRow(rowIdx++);
        XSSFCell groupCell = groupInfoRow.createCell(0);
        groupCell.setCellValue("Староста группы " + reportService.getMonitorName(studyGroupByUserName));
        groupCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 4));

        XSSFCell curatorCell = groupInfoRow.createCell(5);
        curatorCell.setCellValue("Куратор группы ФИО куратора");
        curatorCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 5, 9));

        // Headers
        XSSFRow headerRow = sheet.createRow(rowIdx++);
        String[] headers = {"№ п.п.", "Ф.И.О.", "декабря 2023", "января 2024", "февраля 2024", "марта 2024", "апреля 2024", "мая 2024", "июня 2024", "Итого за семестр (всего / по неуважительной причине)"};
        for (int i = 0; i < headers.length; i++) {
            XSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(borderedStyle);
        }

        // Data rows
        Map<String, Map<Integer, int[]>> reportData = new LinkedHashMap<>();
        Map<String, int[]> totalAttendance = new LinkedHashMap<>();
        for (Object[] row : report) {
            String student = row[0] + " " + row[1] + " " + row[2];
            int year = ((Number) row[3]).intValue();
            int month = ((Number) row[4]).intValue();
            int totalMissedLessons = ((Number) row[5]).intValue();
            int respectMissedStatus = ((Number) row[6]).intValue();

            int monthKey = year * 100 + month;
            reportData.putIfAbsent(student, new LinkedHashMap<>());
            reportData.get(student).put(monthKey, new int[]{totalMissedLessons, respectMissedStatus});

            // Totals
            totalAttendance.putIfAbsent(student, new int[]{0, 0});
            totalAttendance.get(student)[0] += totalMissedLessons;
            totalAttendance.get(student)[1] += respectMissedStatus;
        }

        int studentIndex = 1;
        for (Map.Entry<String, Map<Integer, int[]>> studentStat : reportData.entrySet()) {
            XSSFRow row = sheet.createRow(rowIdx++);
            XSSFCell cell0 = row.createCell(0);
            cell0.setCellValue(studentIndex++);
            cell0.setCellStyle(borderedStyle);

            XSSFCell cell1 = row.createCell(1);
            cell1.setCellValue(studentStat.getKey());
            cell1.setCellStyle(borderedStyle);

            int colIdx = 2;
            for (LocalDate month : getMonthsBetweenDates(startDate, endDate)) {
                int monthKey = month.getYear() * 100 + month.getMonthValue();
                int[] values = studentStat.getValue().getOrDefault(monthKey, new int[]{0, 0});
                XSSFCell cell = row.createCell(colIdx++);
                cell.setCellValue(values[0] + "/" + values[1]);
                cell.setCellStyle(borderedStyle);
            }
            int[] totals = totalAttendance.getOrDefault(studentStat.getKey(), new int[]{0, 0});
            XSSFCell totalCell = row.createCell(colIdx);
            totalCell.setCellValue(totals[0] + "/" + totals[1]);
            totalCell.setCellStyle(borderedStyle);
        }

        // Total row
        XSSFRow totalRow = sheet.createRow(rowIdx++);
        XSSFCell totalLabelCell = totalRow.createCell(0);
        totalLabelCell.setCellValue("Всего пропущено часов");
        totalLabelCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(totalRow.getRowNum(), totalRow.getRowNum(), 0, 1));

        // Add total values per month and semester
        int colIdx = 2;
        for (LocalDate month : getMonthsBetweenDates(startDate, endDate)) {
            int totalMissed = 0;
            int respectMissed = 0;
            for (Map<Integer, int[]> attendance : reportData.values()) {
                int monthKey = month.getYear() * 100 + month.getMonthValue();
                int[] values = attendance.getOrDefault(monthKey, new int[]{0, 0});
                totalMissed += values[0];
                respectMissed += values[1];
            }
            XSSFCell totalMonthCell = totalRow.createCell(colIdx++);
            totalMonthCell.setCellValue(totalMissed + "/" + respectMissed);
            totalMonthCell.setCellStyle(borderedStyle);
        }

        // Total for the semester
        int semesterTotalMissed = 0;
        int semesterRespectMissed = 0;
        for (int[] totals : totalAttendance.values()) {
            semesterTotalMissed += totals[0];
            semesterRespectMissed += totals[1];
        }
        XSSFCell totalSemesterCell = totalRow.createCell(colIdx);
        totalSemesterCell.setCellValue(semesterTotalMissed + "/" + semesterRespectMissed);
        totalSemesterCell.setCellStyle(borderedStyle);

        // Auto-size columns
        for (int i = 0; i <= colIdx; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();

        byte[] fileContent = bos.toByteArray();
        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headersResponse.setContentDispositionFormData("attachment", "semester-attendance-report.xlsx");
        headersResponse.setContentLength(fileContent.length);

        return new ResponseEntity<>(fileContent, headersResponse, HttpStatus.OK);
    }









}
