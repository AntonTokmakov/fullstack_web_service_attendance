package com.diplom.web_service_attendance.repository.security;

import com.diplom.web_service_attendance.entity.securityEntity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
