package com.diplom.web_service_attendance.controller;

import com.diplom.web_service_attendance.Excaption.NotFountStudyGroup;
import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.entity.Lesson;
import com.diplom.web_service_attendance.enumPackage.WeekdayEnum;
import com.diplom.web_service_attendance.repository.LessonRepository;
import com.diplom.web_service_attendance.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.security.InvalidParameterException;
import java.security.Principal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor

@RequestMapping("/lessons")
public class LessonController {

    private final LessonService lessonService;


    @GetMapping("/{requestWeekday}") // надо обработать возможную ошибку
    public ResponseEntity<List<Lesson>> getLessonGroupAndWeekday(@PathVariable String requestWeekday,
                                                                 Principal principal){

        // по авторизованному пользоавателю смотрим какая у него группа и выдаем расписание на день
        int group = 1;
        List<Lesson> lessonList = null;
        WeekdayEnum weekday = null;

        try {
            weekday = WeekdayEnum.valueOf(requestWeekday.toUpperCase());
        } catch (Exception e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }

        try {
            lessonList = lessonService.findLessonsGroupAndWeekday(group, weekday).orElseThrow();
        } catch (NotFountStudyGroup | InvalidParameterException e){
            e.getStackTrace();
        }

        return ResponseEntity.ok(lessonList);
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
