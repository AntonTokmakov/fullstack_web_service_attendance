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
public class Pass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "actual_lesson_id")
    private ActualLesson actualLesson;

    @ManyToOne
    @JoinColumn(name = "document_confirm_id")
    private DocumentConfirm documentConfirm;

    @ManyToOne
    @JoinColumn(name = "status_pass_id")
    private StatusPass statusPass;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

}
