package com.diplom.web_service_attendance.controller;

import com.diplom.web_service_attendance.Excaption.NotFountStudyGroup;
import com.diplom.web_service_attendance.dto.CheckActualLesson;
import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.entity.Lesson;
import com.diplom.web_service_attendance.repository.PassRepository;
import com.diplom.web_service_attendance.service.ActualLessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lessons")
public class LessonController {

    private final ActualLessonService actualLessonService;
    private final PassRepository passRepository;

    @PreAuthorize("hasAuthority('MONITOR')")
    @GetMapping // надо обработать возможную ошибку
    public String getLessonGroupAndWeekday(Principal principal,
                                           Model model){
        List<CheckActualLesson> checkActualLessons = new ArrayList<>();
        LocalDate date = LocalDate.now();
        try {
            List<ActualLesson> lessonList = actualLessonService.findActualLessonByDateAndStudy(date, principal.getName());

            checkActualLessons = lessonList.stream()
                    .map(actualLesson -> CheckActualLesson.builder()
                            .id(actualLesson.getId())
                            .lesson(actualLesson.getLesson())
                            .date(actualLesson.getDate())
                            .isAttendence(passRepository.existsByActualLessonId(actualLesson.getId()))
                            .build())
                    .toList();

        } catch (NotFountStudyGroup | InvalidParameterException e){
            e.getStackTrace();
        }

        model.addAttribute("actualLessons", checkActualLessons);
        return "lessons";
    }

    ////// Обработка исключений

    @ExceptionHandler(NotFountStudyGroup.class)
    public ResponseEntity<ProblemDetail> handleNotFountStudyGroupException(NotFountStudyGroup e){

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                        this.toString()));

    }


}
