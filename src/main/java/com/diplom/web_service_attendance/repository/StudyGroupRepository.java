package com.diplom.web_service_attendance.repository;

import com.diplom.web_service_attendance.entity.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {


    Optional<StudyGroup> findByShortNameLikeIgnoreCase(String shortName);
}
