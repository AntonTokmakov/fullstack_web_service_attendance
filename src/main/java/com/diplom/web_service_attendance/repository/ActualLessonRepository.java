package com.diplom.web_service_attendance.repository;

import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.entity.Lesson;
import com.diplom.web_service_attendance.entity.StudyGroup;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActualLessonRepository extends JpaRepository<ActualLesson, Long> {

    Optional<ActualLesson> findByLessonAndDate(Lesson lessonList, LocalDate date);

    @Query("SELECT al FROM ActualLesson al " +
            "JOIN al.lesson l " +
            "JOIN l.studyGroupList sg " +
            "WHERE al.date =  :date  AND sg.id = :studyGroup")
    List<ActualLesson> findActualLessonsByDateAndStudyGroup(@Param("date") LocalDate date,
                                                            @Param("studyGroup") Long studyGroup);

    @Query("SELECT al FROM ActualLesson al " +
            "JOIN al.lesson l " +
            "JOIN l.teacherList tl " +
            "WHERE al.date = :date  AND tl.id = :teacherId")
    List<ActualLesson> findActualLessonsByDateAndTeacherList(@Param("date") LocalDate date,
                                                             @Param("teacherId") Long teacherId);

    @Query("SELECT al FROM ActualLesson al " +
            "JOIN al.lesson l " +
            "JOIN l.studyGroupList sg " +
            "WHERE al.date BETWEEN :startDate AND :endDate AND sg.id = :studyGroupId")
    List<ActualLesson> findByDateBetweenAndStudyGroup(@Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate,
                                                      @Param("studyGroupId") Long studyGroupId);


//    List<ActualLesson> findByDateAndStudyGroup(LocalDate date, StudyGroup studyGroup);


}
