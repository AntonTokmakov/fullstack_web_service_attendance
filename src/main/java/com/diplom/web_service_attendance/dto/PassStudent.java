package com.diplom.web_service_attendance.dto;

import com.diplom.web_service_attendance.entity.DocumentConfirm;
import com.diplom.web_service_attendance.entity.StudyGroup;
import lombok.Data;

import java.util.Date;

@Data
public class PassStudent {

    private Long id;
    private String firstName;
    private String lastName;
    private String otchestvo;
    private Date birthday;
    private StudyGroup studyGroup;
    private boolean pass;
    private Long documentConfirmId;

}
