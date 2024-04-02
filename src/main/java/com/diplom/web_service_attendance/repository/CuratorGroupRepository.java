package com.diplom.web_service_attendance.repository;

import com.diplom.web_service_attendance.entity.CuratorGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuratorGroupRepository extends JpaRepository<CuratorGroup, Long> {
}
