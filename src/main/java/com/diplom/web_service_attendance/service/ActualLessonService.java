package com.diplom.web_service_attendance.service;

import com.diplom.web_service_attendance.Excaption.NotFountStudyGroup;
import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.repository.ActualLessonRepository;
import com.diplom.web_service_attendance.repository.security.WebUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActualLessonService {

    private final ActualLessonRepository actualLessonRepository;
    private final WebUserRepository userRepository;

    public List<ActualLesson> findActualLessonByDateAndStudy(LocalDate date, String username) throws NotFountStudyGroup {

        Long groupId = userRepository.findByUsername(username).orElseThrow(NotFountStudyGroup::new).getStudyGroup().getId();
        return actualLessonRepository.findActualLessonsByDateAndStudyGroup(date, groupId);

    }

}
