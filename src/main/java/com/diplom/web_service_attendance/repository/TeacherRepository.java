package com.diplom.web_service_attendance.repository;

import com.diplom.web_service_attendance.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findBySecondNameAndFirstNameStartingWithAndOtchestvoStartingWith(String secondName, String firstName, String otchestvo);

}
