package com.diplom.web_service_attendance.repository;

import com.diplom.web_service_attendance.entity.ParityOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParityOfWeekRepository extends JpaRepository<ParityOfWeek, Long> {

    Optional<ParityOfWeek> findByNameIgnoreCase(String name);

}
