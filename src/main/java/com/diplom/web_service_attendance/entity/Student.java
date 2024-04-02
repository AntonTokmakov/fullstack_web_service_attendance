package com.diplom.web_service_attendance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table
@Data
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 64)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 64)
    private String lastName;

    @NotNull
    @Size(min = 2, max = 64)
    private String otchestvo;

    private Date birthday;

    private boolean isMonitor;

    @ManyToOne
    @JoinColumn(name = "study_group_id")
    private StudyGroup studyGroup;

//    @OneToMany
//    @JoinColumn(name = "student_contact_info_id")
//    private List<StudentContact> studentContactList;

}
