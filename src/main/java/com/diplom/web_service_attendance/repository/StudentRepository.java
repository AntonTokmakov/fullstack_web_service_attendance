package com.diplom.web_service_attendance.repository;

import com.diplom.web_service_attendance.entity.DocumentConfirm;
import com.diplom.web_service_attendance.entity.Student;
import com.diplom.web_service_attendance.entity.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByStudyGroup(StudyGroup groupId);

    @Query(value = """
        SELECT s.* FROM student s
        JOIN public.pass p ON s.student_id = p.student_id
        WHERE p.document_confirm_id = :documentConfirmId
    """, nativeQuery = true)
    Student findByPass(long documentConfirmId);
}
