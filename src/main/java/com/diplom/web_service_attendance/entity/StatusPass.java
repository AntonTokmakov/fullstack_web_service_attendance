package com.diplom.web_service_attendance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
public class StatusPass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_pass_id")
    private Long id;
    @NotNull
    @Size(min = 2, max = 50)
    private String name;
    @NotNull
    @Size(min = 2, max = 30)
    private String shortName;

}
