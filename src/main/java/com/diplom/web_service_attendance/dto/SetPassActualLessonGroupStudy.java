package com.diplom.web_service_attendance.dto;

import com.diplom.web_service_attendance.entity.ActualLesson;
import com.diplom.web_service_attendance.entity.Student;
import lombok.Builder;

import java.util.List;

@Builder
public record SetPassActualLessonGroupStudy
        (List<PassStudent> studentList,
         ActualLesson actualLesson){

}
