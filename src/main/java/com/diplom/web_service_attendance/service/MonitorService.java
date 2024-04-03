package com.diplom.web_service_attendance.service;

import com.diplom.web_service_attendance.Excaption.NotFountStudyGroup;
import com.diplom.web_service_attendance.dto.SetPassActualLessonGroupStudy;
import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.entity.Student;
import com.diplom.web_service_attendance.entity.StudyGroup;
import com.diplom.web_service_attendance.entity.securityEntity.WebUser;
import com.diplom.web_service_attendance.repository.ActualLessonRepository;
import com.diplom.web_service_attendance.repository.StudentRepository;
import com.diplom.web_service_attendance.repository.StudyGroupRepository;
import com.diplom.web_service_attendance.repository.security.WebUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonitorService {

    private final StudentRepository studentRepository;
    private final ActualLessonRepository actualLessonRepository;
    private final WebUserRepository userRepository;
    private final StudyGroupRepository studyGroupRepository;

    public SetPassActualLessonGroupStudy getStudentByStudyGroupToPass(String username, long lessonId) throws NotFountStudyGroup {

        WebUser user = userRepository.findByUsername(username).orElseThrow(NotFountStudyGroup::new);
        StudyGroup studyGroup = user.getStudyGroup();
        ActualLesson actualLesson = actualLessonRepository.findById(lessonId).orElse(null);
        List<Student> studentList = studentRepository.findByStudyGroup(studyGroup);
        return SetPassActualLessonGroupStudy.builder()
                .actualLesson(actualLesson)
                .studentList(studentList)
                .build();
    }

    public ActualLesson getActualLessonById(Long lessonId) {
        return actualLessonRepository.findById(lessonId).orElse(null);
    }
}
