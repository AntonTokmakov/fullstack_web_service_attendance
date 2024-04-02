package com.diplom.web_service_attendance.repository;

import com.diplom.web_service_attendance.entity.Weekday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeekdayRepository extends JpaRepository<Weekday, Long> {

    Optional<Weekday> findByNameIgnoreCase(String weekdayEnum);

}
