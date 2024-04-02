package com.diplom.web_service_attendance.repository;

import com.diplom.web_service_attendance.entity.GeneralContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralContactInfoRepository extends JpaRepository<GeneralContactInfo, Long> {
}
