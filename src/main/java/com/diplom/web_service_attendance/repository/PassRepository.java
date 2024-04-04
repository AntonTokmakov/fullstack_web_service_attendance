package com.diplom.web_service_attendance.repository;

import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.entity.Pass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassRepository extends JpaRepository<Pass, Long> {
    List<Pass> deleteAllByStudentIdIn(List<Long> passStudentId);


    boolean existsByStudent(ActualLesson actualLesson);
}
