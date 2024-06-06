package com.diplom.web_service_attendance.service;

import com.diplom.web_service_attendance.entity.DocumentConfirm;
import com.diplom.web_service_attendance.entity.StatusPass;
import com.diplom.web_service_attendance.entity.Student;
import com.diplom.web_service_attendance.entity.StudyGroup;
import com.diplom.web_service_attendance.repository.DocumentConfirmRepository;
import com.diplom.web_service_attendance.repository.StatusPassRepository;
import com.diplom.web_service_attendance.repository.StudentRepository;
import com.diplom.web_service_attendance.repository.security.WebUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final WebUserRepository webUserRepository;
    private final DocumentConfirmRepository documentConfirmRepository;
    private final StudentRepository studentRepository;
    private final StatusPassRepository statusPassRepository;

    public List<DocumentConfirm> getAllReferences(long studyGroupId) {
        return documentConfirmRepository.findAllByStudyGroupId(studyGroupId);
    }

    public StudyGroup getStudyGroupIdByUserName(String username) {
        return webUserRepository.findByUsername(username).orElse(null).getStudyGroup();
    }


    public void saveReference(DocumentConfirm reference) {
        documentConfirmRepository.save(reference);
    }

    public DocumentConfirm getDocumentConfirmById(Long id) {
        return documentConfirmRepository.findById(id).orElse(null);
    }

    public List<Student> getStudentsByStudyGroupId(StudyGroup studyGroup) {
        return studentRepository.findByStudyGroup(studyGroup);
    }

    public List<StatusPass> getAllReasonTypes() {
        return statusPassRepository.findAll();
    }

    public Student getStudent(long documentConfirm) {
        return studentRepository.findByPass(documentConfirm);
    }

    public StatusPass getReason(long documentConfirm) {
        return statusPassRepository.findByStatusPass(documentConfirm);
    }
}
