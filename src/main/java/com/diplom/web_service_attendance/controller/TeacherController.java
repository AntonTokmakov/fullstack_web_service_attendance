package com.diplom.web_service_attendance.controller;

import com.diplom.web_service_attendance.Excaption.NotFountStudyGroup;
import com.diplom.web_service_attendance.dto.CheckActualLesson;
import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.entity.StudyGroup;
import com.diplom.web_service_attendance.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/groups")
    public String getGroups() {

//        List<StudyGroup> studyGroupList = new ArrayList<>();
//        LocalDate date = LocalDate.now();
//        try {
//            List<ActualLesson> lessonList = teacherService.findActualLessonByDate(date, principal.getName());
//
//            checkActualLessons = lessonList.stream()
//                    .map(actualLesson -> CheckActualLesson.builder()
//                            .id(actualLesson.getId())
//                            .lesson(actualLesson.getLesson())
//                            .date(actualLesson.getDate())
//                            .isAttendence(passRepository.existsByActualLessonId(actualLesson.getId()))
//                            .build())
//                    .toList();
//
//        } catch (NotFountStudyGroup | InvalidParameterException e){
//            e.getStackTrace();
//        }
//
//        model.addAttribute("actualLessons", checkActualLessons);
        return null;
    }

}
