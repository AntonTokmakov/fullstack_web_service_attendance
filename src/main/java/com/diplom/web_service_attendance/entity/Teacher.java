package com.diplom.web_service_attendance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private Long id;
    @NotNull
    @Size(min = 1, max = 40)
    private String firstName;
    @NotNull
    @Size(min = 1, max = 50)
    private String secondName;
//    @Size(min = 1, max = 150)
    private String otchestvo;
    @ManyToOne
    @JoinColumn(name = "kafedra_id")
    @JsonIgnore
    private Kafedra kafedra;

//    @JsonIgnore
//    @ManyToMany
//    @JoinTable(
//            name = "appointment_teacher_lesson",
//            joinColumns = @JoinColumn(name = "teacher_id"),
//            inverseJoinColumns = @JoinColumn(name = "lesson_id")
//    )
//    private List<Lesson> lessonList;
}
