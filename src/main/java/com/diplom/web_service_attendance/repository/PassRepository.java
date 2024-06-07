package com.diplom.web_service_attendance.repository;

import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.entity.DocumentConfirm;
import com.diplom.web_service_attendance.entity.Pass;
import com.diplom.web_service_attendance.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    List<Pass> findByActualLessonIn(List<ActualLesson> actualLessons);
    List<Pass> findByActualLesson(ActualLesson actualLessons);

    Pass findByDocumentConfirm(DocumentConfirm documentConfirm);

    @Query(value = """
        SELECT p.*
        FROM pass p
         JOIN public.actual_lesson al on p.actual_lesson_id = al.actual_lesson_id
        WHERE student_id = :studentId AND al.date BETWEEN :startDate AND :endDate
    """, nativeQuery = true)
    List<Pass> findPassByBetweenDates(long studentId, LocalDate startDate, LocalDate endDate);

    @Query(value = """
        SELECT s.last_name, s.first_name, s.otchestvo, s.study_group_id, s.is_monitor, s.student_id,
               COALESCE(COUNT(p.id), 0) AS total_missed_lessons,
               COALESCE(SUM(CASE WHEN sp.status_pass_id = 1 THEN 1 ELSE 0 END), 0) AS respect_missed_status,
               (SELECT COUNT(DISTINCT al2.actual_lesson_id)
                FROM public.actual_lesson al2
                         LEFT JOIN assigning_group_lesson agl ON al2.lesson_id = agl.lesson_id
                         LEFT JOIN lesson l ON al2.lesson_id = l.lesson_id
                         LEFT JOIN assigning_group_lesson agl2 ON l.lesson_id = agl2.lesson_id
                         LEFT JOIN study_group sg ON agl2.study_group_id = sg.study_group_id
                WHERE al2.date BETWEEN :startDate AND :endDate
                  AND sg.study_group_id = s.study_group_id) AS total_lessons
        FROM student s
                 LEFT JOIN pass p on p.student_id = s.student_id AND p.actual_lesson_id IN (
            SELECT al.actual_lesson_id
            FROM public.actual_lesson al
            WHERE al.date BETWEEN :startDate AND :endDate
        )
                 LEFT JOIN public.actual_lesson al on p.actual_lesson_id = al.actual_lesson_id
                 LEFT JOIN status_pass sp on p.status_pass_id = sp.status_pass_id
        WHERE s.study_group_id = :studyGroupId
        GROUP BY s.student_id, s.last_name
        ORDER BY s.last_name
    """, nativeQuery = true)
    List<Object[]> reportAttendance(@Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate,
                                    @Param("studyGroupId") Long studyGroupId);

}
