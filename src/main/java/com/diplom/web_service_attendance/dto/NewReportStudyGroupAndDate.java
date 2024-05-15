package com.diplom.web_service_attendance.dto;

import com.diplom.web_service_attendance.entity.StudyGroup;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class NewReportStudyGroupAndDate {

    List<StudyGroup> studyGroupList;

    LocalDate startDate;

    LocalDate endDate;

}
