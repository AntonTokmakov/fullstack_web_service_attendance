package com.diplom.web_service_attendance.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
public class GeneralContactInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn
    @Column(name = "general_contact_info_id")
    private Long id;

}
