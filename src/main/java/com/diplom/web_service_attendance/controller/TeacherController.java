package com.diplom.web_service_attendance.controller;

import com.diplom.web_service_attendance.Excaption.NotFountStudyGroup;
import com.diplom.web_service_attendance.dto.CheckActualLesson;
import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.entity.StudyGroup;
import com.diplom.web_service_attendance.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.InvalidParameterException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/app/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    @PreAuthorize("hasAuthority('TEACHER')")
    @GetMapping("/groups")
    public String getGroups(Principal principal,
                            Model model) {

        List<StudyGroup> studyGroupList = new ArrayList<>();
        List<ActualLesson> lessonList = new ArrayList<>();
        LocalDate date = LocalDate.now();
        try {

             lessonList = teacherService.findActualLessonByDateAndTeacher(date, principal.getName());

        } catch (InvalidParameterException e){
            e.getStackTrace();
        }

        model.addAttribute("studyGroupList", studyGroupList);
        return "teacher/groups";
    }

}
