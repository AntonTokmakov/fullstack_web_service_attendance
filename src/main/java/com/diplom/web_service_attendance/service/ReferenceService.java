package com.diplom.web_service_attendance.service;

import com.diplom.web_service_attendance.entity.DocumentConfirm;
import com.diplom.web_service_attendance.entity.Student;
import com.diplom.web_service_attendance.entity.StudyGroup;
import com.diplom.web_service_attendance.repository.DocumentConfirmRepository;
import com.diplom.web_service_attendance.repository.security.WebUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReferenceService {

    private final WebUserRepository webUserRepository;

    private final DocumentConfirmRepository documentConfirmRepository;

    public List<DocumentConfirm> getAllReferences(long studyGroupId) {
        return documentConfirmRepository.findAllByStudyGroupId(studyGroupId);
    }

    public StudyGroup getStudyGroupIdByUserName(String username) {
        return webUserRepository.findByUsername(username).orElse(null).getStudyGroup();
    }


    public void saveReference(DocumentConfirm reference) {
        documentConfirmRepository.save(reference);
    }
}
