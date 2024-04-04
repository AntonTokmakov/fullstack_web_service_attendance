package com.diplom.web_service_attendance.dto;

import com.diplom.web_service_attendance.entity.Lesson;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckActualLesson {

    private Long id;
    private Lesson lesson;
    private LocalDate date;
    private Boolean isAttendence;



}

