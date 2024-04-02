package com.diplom.web_service_attendance.repository.security;

import com.diplom.web_service_attendance.entity.securityEntity.WebUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<WebUser, Long> {

    Optional<WebUser> findByUsername(String username);

}
