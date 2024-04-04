package com.diplom.web_service_attendance.repository;

import com.diplom.web_service_attendance.entity.CheckingPass;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckingPassRepository extends JpaRepository<CheckingPass, Long> {


//    CheckingPass findByIdAndChecked(Long lessonId, boolean checked);
    
    CheckingPass findByActualLessonId(Long lessonId);
    
    boolean existsByActualLessonId(Long lessonId);

    boolean deleteByActualLessonId(Long actualLessonId);
}
