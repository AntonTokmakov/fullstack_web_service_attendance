package com.diplom.web_service_attendance.service;

import com.diplom.web_service_attendance.entity.*;
import com.diplom.web_service_attendance.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PassService {

    private final StudentRepository studentRepository;
    private final PassRepository passRepository;
    private final ActualLessonRepository actualLessonRepository;
    private final StatusPassRepository statusPassRepository;
    private final CheckingPassRepository checkingPassRepository;


    @Transactional
    public void savePassActualLesson(Long actualLessonId, List<Long> passStudentId) {

        if (passStudentId == null) {
            return;
        }

        ActualLesson actualLesson = actualLessonRepository.findById(actualLessonId)
                .orElseThrow(() -> new RuntimeException("Актуальное занятие не найдено"));
        List<Student> studentList = studentRepository.findAllById(passStudentId);
        List<Student> filteredStudentList = new ArrayList<>();
        if (passRepository.existsByActualLessonId(actualLessonId)) {
            Pass pass;
            for (Student student : studentList) {
                pass = passRepository.findByActualLessonAndStudent(actualLesson, student);
                if (pass != null && pass.getDocumentConfirm() != null) {
                    filteredStudentList.add(student);
                }
            }
            passRepository.deleteAllByActualLessonAndStudents(actualLesson, filteredStudentList);   //.deleteByActualLessonId(actualLessonId);
        }
//            for (Student student : studentList) {
//                Pass pass1 = passRepository.findByActualLessonAndStudent(actualLesson, student);
//
//            }

//            List<Student> filteredStudentList = studentList.stream()
//                    .filter(student -> (passRepository.findByActualLessonAndStudent(actualLesson, student) != null
//                    && passRepository.findByActualLessonAndStudent(actualLesson, student).getDocumentConfirm() != null))
//                    .collect(Collectors.toList());


        studentList.removeAll(filteredStudentList);

        StatusPass statusPass = statusPassRepository.findByShortNameIgnoreCase("Н");

        List<Pass> passList = new ArrayList<>();
        for (Student student : studentList) {

            passList.add(Pass.builder()
                    .actualLesson(actualLesson)
                    .statusPass(statusPass)
                    .student(student)
                    .build());
        }
        passRepository.saveAll(passList);
    }

    public Boolean getExistsPass(Long id) {
        return passRepository.existsByActualLessonId(id);
    }

    public void setCheckPass(Long lessonId) {

        CheckingPass checkingPass = checkingPassRepository.findByActualLessonId(lessonId);

        if (checkingPass == null) {
            checkingPassRepository.save(CheckingPass.builder()
                    .actualLesson(actualLessonRepository.findById(lessonId).orElse(null))
                    .isChecked(true)
                    .build());
        } else {
            checkingPassRepository.save(checkingPass);
        }
    }

    public boolean existsByActualLessonId(Long actualLessonId) {
        return checkingPassRepository.existsByActualLessonId(actualLessonId);
    }

    public void deleteByActualLessonId(Long actualLessonId) {
        checkingPassRepository.deleteByActualLessonId(actualLessonId);
    }
}
