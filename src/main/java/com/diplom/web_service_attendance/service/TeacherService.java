package com.diplom.web_service_attendance.service;

import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.entity.StudyGroup;
import com.diplom.web_service_attendance.repository.ActualLessonRepository;
import com.diplom.web_service_attendance.repository.security.WebUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService
{

    private final ActualLessonRepository actualLessonRepository;
    private final WebUserRepository userRepository;

    public List<ActualLesson> findActualLessonByDateAndTeacher(LocalDate date, String username) {

        Long teacherId = userRepository.findByUsername(username)
                .orElseThrow().getTeacher().getId();

        return actualLessonRepository.findActualLessonsByDateAndTeacherList(date, teacherId);

    }
}
