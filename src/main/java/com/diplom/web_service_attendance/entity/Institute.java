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
public class Institute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "institute_id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 30)
    private String name;

    @NotNull
    @Size(min = 4, max = 50)
    private String shortName;

//    @Pattern(regexp = "^\\d{3}(?:ГТ|Г)?$")
    private String office;

//    @OneToMany(cascade = CascadeType.ALL,
//            mappedBy = "institute")
//    private List<Kafedra> kafedraList;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "general_contact_info_id")
    private GeneralContactInfo generalContactInfo;
}
