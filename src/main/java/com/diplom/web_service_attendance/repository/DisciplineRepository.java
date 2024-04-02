package com.diplom.web_service_attendance.repository;

import com.diplom.web_service_attendance.entity.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {

    Optional<Discipline> findByNameLikeIgnoreCase(String name);
    Optional<Discipline> findByShortNameLikeIgnoreCase(String name);

}
