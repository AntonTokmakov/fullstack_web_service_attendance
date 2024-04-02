package com.diplom.web_service_attendance.service;

import com.diplom.web_service_attendance.Excaption.NotFountStudyGroup;
import com.diplom.web_service_attendance.entity.*;
import com.diplom.web_service_attendance.enumPackage.ParityWeekEnum;
import com.diplom.web_service_attendance.enumPackage.WeekdayEnum;
import com.diplom.web_service_attendance.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Service
//@RequiredArgsConstructor
//public class LessonAndActualLessonService {
//
//    private final LessonRepository lessonRepository;
//    private final StudyGroupRepository studyGroupRepository;
//    private final WeekdayRepository weekdayRepository;
//    private final ParityOfWeekRepository parityOfWeekRepository;
//    private final ActualLessonRepository actualLessonRepository;
//
//    public List<ActualLesson> findLessonsGroupAndWeekday(Integer group,
//                                                                   String weekdaySt,
//                                                                   ParityWeekEnum parityWeekEnum)
//            throws NotFountStudyGroup, InvalidParameterException {
//
//        WeekdayEnum nowWeekday;
//
//        try {
//            // Попытка установить значение из параметра weekday
//            nowWeekday = WeekdayEnum.valueOf(weekdaySt.toUpperCase());
//        } catch (IllegalArgumentException e) {
//            // Если значение не найдено, устанавливаем текущий день недели
//            DayOfWeek currentDayOfWeek = LocalDate.now().getDayOfWeek();
//            nowWeekday = WeekdayEnum.valueOf(currentDayOfWeek.toString());
//        }
//
//        StudyGroup studyGroup = studyGroupRepository.findById((long) group)
//                .orElseThrow(NotFountStudyGroup::new);
//        Weekday weekday = weekdayRepository.findByNameIgnoreCase(nowWeekday.name())
//                .orElseThrow(() -> new InvalidParameterException("День недели не найден"));
//        ParityOfWeek parityOfWeek = parityOfWeekRepository.findByNameIgnoreCase(parityWeekEnum.name())
//                .orElseThrow(() -> new EntityNotFoundException("Четность недели не найдена"));
//
//
//
//        Optional<List<Lesson>> lessonList = lessonRepository
//                .findLessonsByStudyGroupListAndWeekdayAndParityOfWeekOrderByNumberLesson(
//                        studyGroup,
//                        weekday,
//                        parityOfWeek
//                );
//
//
//        List<ActualLesson> actualLesson = new ArrayList<>();
//        for (Lesson lesson : lessonList.orElse(null)) {
//
//            actualLesson.add(actualLessonRepository.findByLessonAndDate(
//                    lesson,
//                    LocalDate.of(2024, 3, 27)).orElse(null)
//            );
//
//        }
//        return actualLesson;
//
//    }
//
//    public List<ActualLesson> findActualLessonByDateAndStudy(LocalDate date, int groupId) throws NotFountStudyGroup {
//
//        return actualLessonRepository.findActualLessonsByDateAndStudyGroup(date, groupId);
//
//    }
//
//    public Optional<Lesson> findById(int id){
//        return lessonRepository.findById((long) id);
//    }
//}
