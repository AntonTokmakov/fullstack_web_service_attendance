package com.diplom.web_service_attendance.controller;

import com.diplom.web_service_attendance.Excaption.NotFountStudyGroup;
import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.entity.Lesson;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lessons")
public class LessonController {

    private final ActualLessonService actualLessonService;


    @PreAuthorize("hasAuthority('MONITOR')")
    @GetMapping // надо обработать возможную ошибку
    public String getLessonGroupAndWeekday(Principal principal,
                                           Model model){
        List<ActualLesson> lessonList = null;
        LocalDate date = LocalDate.now();
//        String weekday;
//        try {
//            weekday = date.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru"));
//        } catch (Exception e){
//            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
//        }
        try {
            lessonList = actualLessonService.findActualLessonByDateAndStudy(date, principal.getName());
        } catch (NotFountStudyGroup | InvalidParameterException e){
            e.getStackTrace();
        }

        model.addAttribute("actualLessons", lessonList);
        return "lessons";
    }


    @GetMapping("/week2")
    public Lesson getLessonGroupAndWeek2(){
        // по авторизованному пользоавтелю смотрим какая у него группа и выдаем ему расписания на неделю

        return null;
    }


    ////// Обработка исключений

    @ExceptionHandler(NotFountStudyGroup.class)
    public ResponseEntity<ProblemDetail> handleNotFountStudyGroupException(NotFountStudyGroup e){

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                        this.toString()));
    }


}
