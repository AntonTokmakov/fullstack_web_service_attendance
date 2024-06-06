package com.diplom.web_service_attendance.service;

import com.diplom.web_service_attendance.entity.*;
import com.diplom.web_service_attendance.repository.*;
import com.diplom.web_service_attendance.repository.security.WebUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final WebUserRepository webUserRepository;
    private final DocumentConfirmRepository documentConfirmRepository;
    private final PassRepository passRepository;
    private final ActualLessonRepository actualLessonRepository;
    private final StudentRepository studentRepository;
    private final StatusPassRepository statusPassRepository;

    public List<DocumentConfirm> getAllReferences(long studyGroupId) {
        return documentConfirmRepository.findAllByStudyGroupId(studyGroupId);
    }

    public StudyGroup getStudyGroupIdByUserName(String username) {
        return webUserRepository.findByUsername(username).orElse(null).getStudyGroup();
    }


    public void saveDocument(DocumentConfirm reference) {
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

    public void setDocumentConfirmAndPass(DocumentConfirm document, Long studentId, Long reasonId) {

        documentConfirmRepository.save(document);
        Pass pass = passRepository.findByDocumentConfirm(document);
        pass.setStudent(studentRepository.findById(studentId).orElse(null));
        pass.setStatusPass(statusPassRepository.findById(reasonId).orElse(null));
        passRepository.save(pass);

    }

    public void createDocumentConfirmAndPass(DocumentConfirm document, Long studentId, Long reasonId) {

        // у всех предыдущие пропусков меняем статус на уважительную причину
        documentConfirmRepository.save(document);
        List<Pass> passList = passRepository
                .findPassByBetweenDates(studentId, document.getStartDate(), document.getEndDate());
        for (Pass pass : passList) {
            pass.setDocumentConfirm(document);
            pass.setStatusPass(statusPassRepository.findById(reasonId).orElse(null));
            passRepository.save(pass);
        }

        // у всех будущих пар по дате создаем пропуск и ставим статус на уважительную причину
        List<ActualLesson> actualLessonList = actualLessonRepository
                .findActualLessonsByStudent(studentId, LocalDate.now().plusDays(1), document.getEndDate());
        for (ActualLesson actualLesson : actualLessonList) {
            Pass pass = new Pass();
            pass.setActualLesson(actualLesson);
            pass.setDocumentConfirm(document);
            pass.setStudent(studentRepository.findById(studentId).orElse(null));
            pass.setStatusPass(statusPassRepository.findById(reasonId).orElse(null));
            passRepository.save(pass);
        }


    }
}
