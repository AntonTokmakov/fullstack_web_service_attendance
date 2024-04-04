package com.diplom.web_service_attendance.repository;

import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.entity.Pass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassRepository extends JpaRepository<Pass, Long> {
    List<Pass> deleteAllByStudentIdIn(List<Long> passStudentId);

//    boolean deleteByActualLessonId(Long actualLesson);

    @Modifying
    @Query("DELETE FROM Pass p WHERE p.actualLesson.id = :idActualLesson")
    void deleteByActualLessonId(@Param("idActualLesson") Long idActualLesson);

    boolean existsByActualLessonId(Long actualLesson);

    boolean existsByActualLessonIdAndStudentId(Long lessonId, Long id);
}
