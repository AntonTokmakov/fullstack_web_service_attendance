package com.diplom.web_service_attendance.dto;

import com.diplom.web_service_attendance.entity.StudyGroup;
import com.diplom.web_service_attendance.entity.securityEntity.Authority;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReturnRegisterMonitor {

    private String username;
    private String password;
    private StudyGroup studyGroup;
    private List<Authority> authority;

}
