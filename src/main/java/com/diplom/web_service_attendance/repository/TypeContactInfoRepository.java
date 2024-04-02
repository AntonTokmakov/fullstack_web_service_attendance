package com.diplom.web_service_attendance.repository;

import com.diplom.web_service_attendance.entity.TypeContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeContactInfoRepository extends JpaRepository<TypeContactInfo, Long> {
}
