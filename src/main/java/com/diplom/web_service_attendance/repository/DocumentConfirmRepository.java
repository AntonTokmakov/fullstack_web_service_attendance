package com.diplom.web_service_attendance.repository;

import com.diplom.web_service_attendance.entity.DocumentConfirm;
import com.diplom.web_service_attendance.entity.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentConfirmRepository extends JpaRepository<DocumentConfirm, Long> {
    @Query(value = """
        SELECT dc.*
        FROM document_confirm dc
                 JOIN public.pass p ON dc.document_confirm_id = p.document_confirm_id
                 JOIN public.student s ON p.student_id = s.student_id
        WHERE s.study_group_id = :studyGroupId
        """, nativeQuery = true)
    List<DocumentConfirm> findAllByStudyGroupId(Long studyGroupId);
}
