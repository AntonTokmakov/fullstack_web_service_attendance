package com.diplom.web_service_attendance.service;

import com.diplom.web_service_attendance.dto.RegisterMonitor;
import com.diplom.web_service_attendance.dto.ReturnRegisterMonitor;
import com.diplom.web_service_attendance.entity.StudyGroup;
import com.diplom.web_service_attendance.entity.securityEntity.Authority;
import com.diplom.web_service_attendance.entity.securityEntity.WebUser;
import com.diplom.web_service_attendance.repository.StudyGroupRepository;
import com.diplom.web_service_attendance.repository.security.AuthorityRepository;
import com.diplom.web_service_attendance.repository.security.WebUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final WebUserRepository webUserRepository;
    private final AuthorityRepository authorityRepository;
    private final StudyGroupRepository studyGroupRepository;

    public RegisterMonitor createDefaultMonitor() {

        return RegisterMonitor.builder()
                /*.authority(authorityRepository.findByAuthorityIgnoreCase("ROLE_MONITOR"))*/
                .studyGroup(studyGroupRepository.findAll())
                .build();
    }

    public void createMonitor(ReturnRegisterMonitor registerMonitor) {

        WebUser webUser = WebUser.builder()
                .username(registerMonitor.getUsername())
                .password("{noop}" + registerMonitor.getPassword())
                .studyGroup(registerMonitor.getStudyGroup())
                .build();
        webUser.addAuthority(authorityRepository.findByAuthorityIgnoreCase("ROLE_MONITOR"));
        webUserRepository.save(webUser);

    }
}
