package com.diplom.web_service_attendance.controller;

import com.diplom.web_service_attendance.Excaption.NotFountStudyGroup;
import com.diplom.web_service_attendance.dto.SetPassActualLessonGroupStudy;
import com.diplom.web_service_attendance.entity.Lesson;
import com.diplom.web_service_attendance.enumPackage.WeekdayEnum;
import com.diplom.web_service_attendance.service.MonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.security.InvalidParameterException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("lessons/{requestWeekday}/monitor")
public class MonitorController {

    private final MonitorService monitorService;


    @GetMapping("/pass")
    public SetPassActualLessonGroupStudy getStudentToPass(@RequestParam("lessonId") Long lessonId){

        String username = "anton";
        try {
            return monitorService.getStudentByStudyGroupToPass(username, lessonId);
        } catch (NotFountStudyGroup e) {
            throw new RuntimeException(e);
        }
    }

}
