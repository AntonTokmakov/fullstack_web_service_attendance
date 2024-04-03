package com.diplom.web_service_attendance.controller;

import com.diplom.web_service_attendance.Excaption.NotFountStudyGroup;
import com.diplom.web_service_attendance.dto.SetPassActualLessonGroupStudy;
import com.diplom.web_service_attendance.entity.Lesson;
import com.diplom.web_service_attendance.entity.Student;
import com.diplom.web_service_attendance.enumPackage.WeekdayEnum;
import com.diplom.web_service_attendance.service.MonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.security.InvalidParameterException;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("lessons/monitor")
public class MonitorController {

    private final MonitorService monitorService;


    @PreAuthorize("hasAuthority('MONITOR')")
    @GetMapping("/pass/{lessonId}")
    public String getStudentToPass(@PathVariable("lessonId") Long lessonId,
                                                          Principal principal,
                                                          Model model) {

        String username = principal.getName();
        SetPassActualLessonGroupStudy pass = null;
        try {
            pass = monitorService.getStudentByStudyGroupToPass(username, lessonId);
        } catch (NotFountStudyGroup e) {
            throw new RuntimeException(e);
        }

        model.addAttribute("setPassActualLessonGroupStudy", pass);
        return "lessons.monitor.pass2";
    }

    @PreAuthorize("hasAuthority('MONITOR')")
//    @PostMapping("/pass/{lessonId}")
    @PostMapping("/pass")
    public ResponseEntity<String> setPassActualLessonGroupStudy(@RequestBody Set<Long> studentsWithPass) {
        try {
            monitorService.setPassActualLessonGroupStudy(studentsWithPass);
            return ResponseEntity.ok("Успешно обновлено");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обновлении");
        }
    }

}
