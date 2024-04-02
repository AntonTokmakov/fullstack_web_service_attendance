package com.diplom.web_service_attendance.repository;

import com.diplom.web_service_attendance.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {


    Optional<List<Lesson>> findLessonsByStudyGroupListAndWeekdayAndParityOfWeekOrderByNumberLesson(StudyGroup studyGroup, Weekday weekday, ParityOfWeek parityOfWeek);

    Optional<List<Lesson>> findLessonsByStudyGroupListAndWeekdayOrderByNumberLesson(StudyGroup studyGroup, Weekday weekday);
}
