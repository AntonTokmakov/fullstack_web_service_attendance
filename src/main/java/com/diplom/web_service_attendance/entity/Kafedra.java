package com.diplom.web_service_attendance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
public class Kafedra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kafedra_id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 30)
    private String name;

    @NotNull
    @Size(min = 1, max = 20)
    private String shortName;

//    @Pattern(regexp = "^\\d{3}(?:ГТ|Г)?$")
    private String office;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "institute_id")
    @JsonIgnore
    private Institute institute;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "general_contact_info_id")
    private GeneralContactInfo generalContactInfo;



//    @OneToMany
//    @JoinColumn(name = "study_group_id")
//    private List<StudyGroup> studyGroupList;
//
//    @OneToMany
//    @JoinColumn(name = "discipline_id")
//    private List<Discipline> disciplineList;
//
//    @OneToMany
//    @JoinColumn(name = "teacher_id")
//    private List<Teacher> teacherList;

}

