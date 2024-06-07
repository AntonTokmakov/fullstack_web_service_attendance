package com.diplom.web_service_attendance.controller;

import com.diplom.web_service_attendance.Excaption.NotActualLessonsDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotActualLessonsDocument.class)
    public String notActualLessonsDocument(NotActualLessonsDocument e, Model model) {
        model.addAttribute("message", e.getMessage());
        log.error(e.getMessage());
        return "error/NotActualLessonsDocument";
    }


}

