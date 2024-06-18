package com.diplom.web_service_attendance.service;

import com.diplom.web_service_attendance.dto.NewReportStudyGroupAndDate;
import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.entity.Pass;
import com.diplom.web_service_attendance.entity.Student;
import com.diplom.web_service_attendance.entity.StudyGroup;
import com.diplom.web_service_attendance.entity.securityEntity.WebUser;
import com.diplom.web_service_attendance.repository.ActualLessonRepository;
import com.diplom.web_service_attendance.repository.PassRepository;
import com.diplom.web_service_attendance.repository.StudentRepository;
import com.diplom.web_service_attendance.repository.StudyGroupRepository;
import com.diplom.web_service_attendance.repository.security.WebUserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ActualLessonRepository actualLessonRepository;
    private final PassRepository passRepository;
    private final WebUserRepository webUserRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final StudentRepository studentRepository;

    public List<Object[]> getAttendanceReport(LocalDate startDate, LocalDate endDate, Long studyGroupId) {
        return passRepository.reportAttendance(startDate, endDate, studyGroupId);
    }

    public List<Object[]> getSemesterAttendanceReport(LocalDate startDate, LocalDate endDate, Long studyGroupId) {
        return passRepository.reportAttendanceByMonth(startDate, endDate, studyGroupId);
    }


    public StudyGroup getStudyGroupIdByUserName(String username) {
        return webUserRepository.findByUsername(username).orElse(null).getStudyGroup();
    }

    public Map<Student, Integer> getReportAttendance(String username) {

        Long studyGroupId = webUserRepository.findByUsername(username).orElse(null).getStudyGroup().getId();

        List<ActualLesson> actualLessonList = actualLessonRepository.findByDateBetweenAndStudyGroup(LocalDate.now().minusMonths(1), LocalDate.now(), studyGroupId);

        return countAbsencesByStudents(actualLessonList);
    }

    public Map<Student, Integer> countAbsencesByStudents(List<ActualLesson> actualLessonList) {
        Map<Student, Integer> studentAbsencesMap = new HashMap<>();

        for (ActualLesson actualLesson : actualLessonList) {
            List<Pass> passes = passRepository.findByActualLesson(actualLesson);

            for (Pass pass : passes) {
                Student student = pass.getStudent();

                if (studentAbsencesMap.containsKey(student)) {
                    studentAbsencesMap.put(student, studentAbsencesMap.get(student) + 1);
                } else {
                    studentAbsencesMap.put(student, 1);
                }
            }
        }

        return studentAbsencesMap;
    }


    public NewReportStudyGroupAndDate getNewReportStudyGroupAndDate() {

        return NewReportStudyGroupAndDate.builder()
                .studyGroupList(studyGroupRepository.findAll())
                .startDate(LocalDate.now().minusMonths(1).plusDays(1))
                .endDate(LocalDate.now())
                .build();
    }


    public Map<Student, Integer> getReportTeacher(String username) {

        Long studyGroupId = webUserRepository.findByUsername(username).orElse(null).getStudyGroup().getId();

        List<ActualLesson> actualLessonList = actualLessonRepository.findByDateBetweenAndStudyGroup(LocalDate.now().minusMonths(1), LocalDate.now(), studyGroupId);

        return countAbsencesByStudents(actualLessonList);
    }

    public ByteArrayInputStream createExcelReport(List<Object[]> report, LocalDate startDate, LocalDate endDate, StudyGroup studyGroup) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Отчет по посещаемости");

            // Заголовки
            Row header1 = sheet.createRow(0);
            Cell header1Cell = header1.createCell(0);
            header1Cell.setCellValue("Сводная ведомость учета посещаемости занятий студентами группы ИСП-20");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

            Row header2 = sheet.createRow(1);
            Cell header2Cell = header2.createCell(0);
            header2Cell.setCellValue("(всего / по неуважительной причине)");
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));

            Row header3 = sheet.createRow(2);
            Cell header3Cell = header3.createCell(0);
            header3Cell.setCellValue("Отчет за период с " + startDate + " по " + endDate);

            String monitorName = getMonitorName(studyGroup);

            header3Cell.setCellValue("Староста группы: " + monitorName);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 4));

            Row header4 = sheet.createRow(4);
            header4.createCell(0).setCellValue("Фамилия");
            header4.createCell(1).setCellValue("Имя");
            header4.createCell(2).setCellValue("Отчество");
            header4.createCell(3).setCellValue("Всего пропусков");
            header4.createCell(4).setCellValue("Пропуски по УП");

            // Данные
            int rowNum = 5;
            int totalMissedLessons = 0;
            int totalRespectMissedStatus = 0;

            for (Object[] row : report) {
                Row dataRow = sheet.createRow(rowNum++);
                dataRow.createCell(0).setCellValue(row[0].toString());
                dataRow.createCell(1).setCellValue(row[1].toString());
                dataRow.createCell(2).setCellValue(row[2].toString());
                dataRow.createCell(3).setCellValue(((Number) row[6]).intValue());
                dataRow.createCell(4).setCellValue(((Number) row[7]).intValue());

                totalMissedLessons += ((Number) row[6]).intValue();
                totalRespectMissedStatus += ((Number) row[7]).intValue();
            }

            // Итого
            Row totalRow = sheet.createRow(rowNum);
            totalRow.createCell(0).setCellValue("Итого пропусков на группу:");
            totalRow.createCell(3).setCellValue(totalMissedLessons);
            totalRow.createCell(4).setCellValue(totalRespectMissedStatus);
            sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 2));

            // Автоматическое подстраивание ширины столбцов
            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            // Добавление границ
            CellStyle borderStyle = workbook.createCellStyle();
            borderStyle.setBorderBottom(BorderStyle.THIN);
            borderStyle.setBorderTop(BorderStyle.THIN);
            borderStyle.setBorderRight(BorderStyle.THIN);
            borderStyle.setBorderLeft(BorderStyle.THIN);

            for (int i = 4; i <= rowNum; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < 5; j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        cell = row.createCell(j);
                    }
                    cell.setCellStyle(borderStyle);
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public String getMonitorName(StudyGroup studyGroup) {
        List<Student> studentGroupList = studentRepository.findByStudyGroup(studyGroup);
        Student monitor = studentGroupList.stream()
                .filter(Student::isMonitor)
                .findFirst().orElse(null);
        String monitorName = monitor == null ? "" : monitor.getFirstName() + " "
                + monitor.getLastName() + " " + monitor.getOtchestvo();
        return monitorName;
    }


    public List<StudyGroup> getStudyGroups() {
        return studyGroupRepository.findAll();
    }

    public StudyGroup getStudyGroup(Long studyGroupId) {
        return studyGroupRepository.findById(studyGroupId).orElse(null);
    }
}
