package com.diplom.web_service_attendance.repository.security;

import com.diplom.web_service_attendance.entity.securityEntity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findByAuthorityIgnoreCase(String name);
}
