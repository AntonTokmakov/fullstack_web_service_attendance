package com.diplom.web_service_attendance.repository;

import com.diplom.web_service_attendance.entity.Student;
import com.diplom.web_service_attendance.entity.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByStudyGroup(StudyGroup groupId);
}
