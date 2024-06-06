package com.diplom.web_service_attendance.controller;

import com.diplom.web_service_attendance.Excaption.NotFountStudyGroup;
import com.diplom.web_service_attendance.dto.CheckActualLesson;
import com.diplom.web_service_attendance.dto.SetPassActualLessonGroupStudy;
import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.entity.DocumentConfirm;
import com.diplom.web_service_attendance.entity.StudyGroup;
import com.diplom.web_service_attendance.repository.PassRepository;
import com.diplom.web_service_attendance.service.ActualLessonService;
import com.diplom.web_service_attendance.service.MonitorService;
import com.diplom.web_service_attendance.service.PassService;
import com.diplom.web_service_attendance.service.DocumentService;
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
@RequestMapping("app/monitor")
public class MonitorController {

    private final MonitorService monitorService;
    private final PassService passService;
    private final ActualLessonService actualLessonService;
    private final PassRepository passRepository;

    private final DocumentService documentService;

    @PreAuthorize("hasAuthority('MONITOR')")
    @GetMapping("/lessons")
    public String getLessonGroupAndWeekday(Principal principal,
                                           Model model){
        List<CheckActualLesson> checkActualLessons = new ArrayList<>();
        LocalDate date = LocalDate.of(2024, 4, 9); //LocalDate.now();//      todo - delete
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
    public String handlePassFormSubmission(@PathVariable("lessonId") Long actualLessonId,
                                            @RequestParam(value = "studentList", required = false, defaultValue = "") String[] passStudentId) {


        List<Long> passStudentIdList = Arrays.stream(passStudentId).map(Long::parseLong).collect(Collectors.toList());
        passService.savePassActualLesson(actualLessonId, passStudentIdList);

        return "redirect:/app/monitor/lessons";
    }




    @GetMapping("/documents")
    public String listReferences(Model model, Principal principal) {
        StudyGroup studyGroupIdByUserName = documentService.getStudyGroupIdByUserName(principal.getName());
        List<DocumentConfirm> references = documentService.getAllReferences(studyGroupIdByUserName.getId());
        model.addAttribute("documents", references);
        return "documentList";
    }

    @GetMapping("/documents/create")
    public String showCreateForm(Model model) {
        model.addAttribute("document", new DocumentConfirm());
        return "createDocument";
    }

    @PostMapping("/documents/create")
    public String createReference(@ModelAttribute DocumentConfirm reference) {
        documentService.saveReference(reference);
        return "redirect:/documents";
    }

    @GetMapping("documents/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model, Principal principal) {
        StudyGroup studyGroupIdByUserName = documentService.getStudyGroupIdByUserName(principal.getName());
        DocumentConfirm documentConfirm = documentService.getDocumentConfirmById(id);
        System.err.println("aaaaaaaaaaaaa");
        Long studentId = documentService.getStudent(documentConfirm.getId()) !=
                null ? documentService.getStudent(documentConfirm.getId()).getId() : null;
        System.err.println("ffffffffffffffff");
        Long reasonId = documentService.getReason(documentConfirm.getId()) !=
                null ? documentService.getReason(documentConfirm.getId()).getId() : null;
        System.err.println("dddddddddd");
        model.addAttribute("document", documentConfirm);
        System.err.println("document good");
        model.addAttribute("students", documentService.getStudentsByStudyGroupId(studyGroupIdByUserName));
        System.err.println("students good");
        model.addAttribute("reasons", documentService.getAllReasonTypes());
        System.err.println("reasons good");
        model.addAttribute("selectedStudentId", studentId);
        model.addAttribute("selectedReasonId", reasonId);

        return "updateDocument";
    }

// todo работает
//    @PostMapping("documents/{id}")
//    public String updateReference(@PathVariable("id") Long id, @ModelAttribute DocumentConfirm reference) {
//        referenceService.saveReference(reference);
//        return "redirect:/documents";
//    }





    @GetMapping("documents/delete/{id}")
    public String deleteReference(@PathVariable("id") Long id) {
//        referenceService.deleteReference(id);
        return "redirect:/documents";
    }

}
