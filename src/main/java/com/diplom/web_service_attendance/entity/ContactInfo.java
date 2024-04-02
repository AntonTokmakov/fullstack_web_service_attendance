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
public class ContactInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_info_id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "type_contact_id")
    private TypeContactInfo typeContact;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "general_contact_info_id")
    private GeneralContactInfo generalContactInfo;

    @NotNull
    @Size(min = 2, max = 100)
    private String contactInfo;
}
