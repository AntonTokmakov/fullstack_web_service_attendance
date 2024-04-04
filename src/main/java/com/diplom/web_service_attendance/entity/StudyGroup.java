package com.diplom.web_service_attendance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_group_id")
    private Long id;

    @Size(min = 2, max = 30)
    @Column(unique = true)
    private String name;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(unique = true)
    private String shortName;

    @Max(50)
    private Integer groupSize;

    @ManyToOne
    @JoinColumn(name = "kafedra_id")
    @JsonIgnore
    private Kafedra kafedra;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;

    @NotNull
    private LocalDate yearAdmission;

//    @JsonIgnore
//    @ManyToMany
//    @JoinTable(
//            name = "assigning_group_lesson",
//            joinColumns = @JoinColumn(name = "study_group_id"),
//            inverseJoinColumns = @JoinColumn(name = "lesson_id")
//    )
//    private List<Lesson> lessonList;

}
