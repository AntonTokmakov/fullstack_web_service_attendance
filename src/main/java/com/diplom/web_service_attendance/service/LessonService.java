package com.diplom.web_service_attendance.service;

import com.diplom.web_service_attendance.Excaption.NotFountStudyGroup;
import com.diplom.web_service_attendance.entity.Lesson;
import com.diplom.web_service_attendance.entity.StudyGroup;
import com.diplom.web_service_attendance.entity.Weekday;
import com.diplom.web_service_attendance.enumPackage.WeekdayEnum;
import com.diplom.web_service_attendance.repository.LessonRepository;
import com.diplom.web_service_attendance.repository.StudyGroupRepository;
import com.diplom.web_service_attendance.repository.WeekdayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final WeekdayRepository weekdayRepository;

    public Optional<List<Lesson>> findLessonsGroupAndWeekday(Integer group, WeekdayEnum weekdayEnum) throws NotFountStudyGroup, InvalidParameterException {

        StudyGroup studyGroup = studyGroupRepository.findById((long) group).orElseThrow(() -> new NotFountStudyGroup());
        Weekday weekday = weekdayRepository.findByNameIgnoreCase(weekdayEnum.name()).orElseThrow(() -> new InvalidParameterException("День недели не найден"));

        return lessonRepository.findLessonsByStudyGroupListAndWeekdayOrderByNumberLesson(studyGroup, weekday);

    }

    public Optional<Lesson> findById(int id){
        return lessonRepository.findById((long) id);
    }
}
