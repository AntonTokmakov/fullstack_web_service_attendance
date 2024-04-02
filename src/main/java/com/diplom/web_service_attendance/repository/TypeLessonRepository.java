package com.diplom.web_service_attendance.repository;

import com.diplom.web_service_attendance.entity.TypeLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeLessonRepository extends JpaRepository<TypeLesson, Long> {

    Optional<TypeLesson> findByName(String name);

}
