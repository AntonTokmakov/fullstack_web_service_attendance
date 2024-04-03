package com.diplom.web_service_attendance.dto.mapper;

import com.diplom.web_service_attendance.dto.PassStudent;
import com.diplom.web_service_attendance.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    PassStudent toPassStudent(Student student);

    Student convertToStudent(PassStudent passStudent);

    default List<Student> convertToStudentList(List<PassStudent> passStudents) {
        return passStudents.stream()
                .map(this::convertToStudent)
                .collect(Collectors.toList());
    }

    default List<PassStudent> convertToPassStudentList(List<Student> students) {
        return students.stream()
                .map(this::toPassStudent)
                .collect(Collectors.toList());
    }

}
