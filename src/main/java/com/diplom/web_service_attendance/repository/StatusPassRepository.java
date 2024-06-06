package com.diplom.web_service_attendance.repository;

import com.diplom.web_service_attendance.entity.DocumentConfirm;
import com.diplom.web_service_attendance.entity.StatusPass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusPassRepository extends JpaRepository<StatusPass, Long> {

    StatusPass findByShortNameIgnoreCase(String statusName);

    @Query(value = """
        SELECT sp.* FROM pass p
        JOIN status_pass sp ON p.status_pass_id = sp.status_pass_id
        WHERE document_confirm_id = :documentConfirmId
""", nativeQuery = true)
    StatusPass findByStatusPass(long documentConfirmId);
}
