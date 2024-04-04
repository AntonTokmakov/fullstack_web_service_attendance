package com.diplom.web_service_attendance.controller;

import com.diplom.web_service_attendance.Excaption.NotFountStudyGroup;
import com.diplom.web_service_attendance.dto.SetPassActualLessonGroupStudy;
import com.diplom.web_service_attendance.service.MonitorService;
import com.diplom.web_service_attendance.service.PassService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("lessons/monitor")
public class MonitorController {

    private final MonitorService monitorService;
    private final PassService passService;


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
        return "lessons.monitor.pass3";
    }

    @PreAuthorize("hasAuthority('MONITOR')")
    @PostMapping("/pass/{lessonId}")
    public String handlePassFormSubmission(@PathVariable("lessonId") Long lessonId,
                                            @RequestParam("studentList") String[] passStudentId) {

        List<Long> passStudentIdList = Arrays.stream(passStudentId).map(Long::parseLong).collect(Collectors.toList());
        passService.savePassActualLesson(lessonId, passStudentIdList);

        return "redirect:/lessons";
    }


}
