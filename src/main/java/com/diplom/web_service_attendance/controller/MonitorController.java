package com.diplom.web_service_attendance.controller;

import com.diplom.web_service_attendance.Excaption.NotFountStudyGroup;
import com.diplom.web_service_attendance.dto.SetPassActualLessonGroupStudy;
import com.diplom.web_service_attendance.service.MonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/monitor")
public class MonitorController {

    private final MonitorService monitorService;


    @GetMapping("/student/pass")
    public SetPassActualLessonGroupStudy getStudentToPass(@RequestParam("lessonId") Long lessonId){

        String username = "anton";
        try {
            return monitorService.getStudentByStudyGroupToPass(username, lessonId);
        } catch (NotFountStudyGroup e) {
            throw new RuntimeException(e);
        }
    }

}
