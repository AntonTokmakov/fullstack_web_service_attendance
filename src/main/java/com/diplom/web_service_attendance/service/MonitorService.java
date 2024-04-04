package com.diplom.web_service_attendance.service;

import com.diplom.web_service_attendance.Excaption.NotFountStudyGroup;
import com.diplom.web_service_attendance.dto.PassStudent;
import com.diplom.web_service_attendance.dto.SetPassActualLessonGroupStudy;
import com.diplom.web_service_attendance.dto.mapper.StudentMapper;
import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.entity.Student;
import com.diplom.web_service_attendance.entity.StudyGroup;
import com.diplom.web_service_attendance.entity.securityEntity.WebUser;
import com.diplom.web_service_attendance.repository.*;
import com.diplom.web_service_attendance.repository.security.WebUserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MonitorService {

    private final StudentRepository studentRepository;
    private final ActualLessonRepository actualLessonRepository;
    private final WebUserRepository userRepository;
    private final PassRepository passRepository;

    public SetPassActualLessonGroupStudy getStudentByStudyGroupToPass(String username, long lessonId) throws NotFountStudyGroup {

        WebUser user = userRepository.findByUsername(username).orElseThrow(NotFountStudyGroup::new);
        StudyGroup studyGroup = user.getStudyGroup();
        ActualLesson actualLesson = actualLessonRepository.findById(lessonId).orElse(null);
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

    public ActualLesson getActualLessonById(Long lessonId) {
        return actualLessonRepository.findById(lessonId).orElse(null);
    }

    public void setPassActualLessonGroupStudy(Set<Long> studentsWithPass) {
        for(Long id : studentsWithPass) {
            Student student = studentRepository.findById(id).orElse(null);

            System.out.println(student.getFirstName() + "\n");
        }
    }
}
