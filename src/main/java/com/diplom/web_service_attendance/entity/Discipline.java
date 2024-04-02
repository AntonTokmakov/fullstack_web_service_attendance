package com.diplom.web_service_attendance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discipline_id")
    private Long id;

    @Size(min = 2, max = 80)
    private String name;

    @NotNull
    @Size(min = 1, max = 50)
    private String shortName;

    @ManyToOne()
    @JoinColumn(name = "kafedra_id")
    private Kafedra kafedra;
}
