package com.diplom.web_service_attendance.service;

import com.diplom.web_service_attendance.Excaption.NotFountStudyGroup;
import com.diplom.web_service_attendance.dto.PassStudent;
import com.diplom.web_service_attendance.dto.SetPassActualLessonGroupStudy;
import com.diplom.web_service_attendance.dto.mapper.StudentMapper;
import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.entity.StudyGroup;
import com.diplom.web_service_attendance.entity.securityEntity.WebUser;
import com.diplom.web_service_attendance.repository.ActualLessonRepository;
import com.diplom.web_service_attendance.repository.PassRepository;
import com.diplom.web_service_attendance.repository.StudentRepository;
import com.diplom.web_service_attendance.repository.security.WebUserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService
{

    private final ActualLessonRepository actualLessonRepository;
    private final WebUserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PassRepository passRepository;

    public List<ActualLesson> findActualLessonByDateAndTeacher(LocalDate date, String username) {

        Long teacherId = userRepository.findByUsername(username)
                .orElseThrow().getTeacher().getId();

        return actualLessonRepository.findActualLessonsByDateAndTeacherList(date, teacherId);

    }

    public SetPassActualLessonGroupStudy getStudentByStudyGroupToPass(String username, long lessonId) throws NotFountStudyGroup {

        WebUser user = userRepository.findByUsername(username).orElseThrow(NotFountStudyGroup::new);
        StudyGroup studyGroup = user.getStudyGroup();
        ActualLesson actualLesson = actualLessonRepository.findById(lessonId).orElse(null);
        // подключил мапер
        StudentMapper studentMapper = Mappers.getMapper(StudentMapper.class);
        List<PassStudent> studentList = studentMapper.convertToPassStudentList(studentRepository.findByStudyGroup(studyGroup));
        for (PassStudent student : studentList) {
            student.setPass(passRepository.existsByActualLessonIdAndStudentId(lessonId, student.getId()));
        }
        return SetPassActualLessonGroupStudy.builder()
                .actualLesson(actualLesson)
                .studentList(studentList)
                .build();
    }


}
