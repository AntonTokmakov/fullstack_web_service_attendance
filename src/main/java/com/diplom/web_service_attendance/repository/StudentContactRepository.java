package com.diplom.web_service_attendance.repository;

import com.diplom.web_service_attendance.entity.StudentContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentContactRepository extends JpaRepository<StudentContact, Long> {
}
