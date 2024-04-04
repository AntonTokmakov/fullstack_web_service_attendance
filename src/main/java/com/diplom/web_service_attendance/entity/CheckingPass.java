package com.diplom.web_service_attendance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class CheckingPass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checking_pass_id")
    private Long id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "pass_id")
    private ActualLesson actualLesson;

    @NotNull
    private boolean isChecked;
}
