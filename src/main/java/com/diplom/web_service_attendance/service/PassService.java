package com.diplom.web_service_attendance.service;

import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.entity.Pass;
import com.diplom.web_service_attendance.entity.StatusPass;
import com.diplom.web_service_attendance.entity.Student;
import com.diplom.web_service_attendance.repository.ActualLessonRepository;
import com.diplom.web_service_attendance.repository.PassRepository;
import com.diplom.web_service_attendance.repository.StatusPassRepository;
import com.diplom.web_service_attendance.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PassService {

    private final StudentRepository studentRepository;
    private final PassRepository passRepository;
    private final ActualLessonRepository actualLessonRepository;
    private final StatusPassRepository statusPassRepository;


    @Transactional
    public void savePassActualLesson(Long lessonId, List<Long> passStudentId) {



        if (passRepository.existsByActualLessonId(lessonId)) {
            passRepository.deleteByActualLessonId(lessonId);
        }

        if (passStudentId == null) {
            return;
        }

        ActualLesson actualLesson = actualLessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Актуальное занятие не найдено"));
        passRepository.deleteAllByStudentIdIn(passStudentId);
        List<Student> studentList = studentRepository.findAllById(passStudentId);
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
}
