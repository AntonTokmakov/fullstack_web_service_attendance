package com.diplom.web_service_attendance.service;

import com.diplom.web_service_attendance.dto.NewReportStudyGroupAndDate;
import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.entity.Pass;
import com.diplom.web_service_attendance.entity.Student;
import com.diplom.web_service_attendance.entity.StudyGroup;
import com.diplom.web_service_attendance.entity.securityEntity.WebUser;
import com.diplom.web_service_attendance.repository.ActualLessonRepository;
import com.diplom.web_service_attendance.repository.PassRepository;
import com.diplom.web_service_attendance.repository.StudyGroupRepository;
import com.diplom.web_service_attendance.repository.security.WebUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public List<Object[]> getAttendanceReport(LocalDate startDate, LocalDate endDate, Long studyGroupId) {
        return passRepository.reportAttendance(startDate, endDate, studyGroupId);
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
}
