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
import com.diplom.web_service_attendance.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/app/teacher")
public class TeacherController {

    private final TeacherService teacherService;
    private final PassService passService;

    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;


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

}
