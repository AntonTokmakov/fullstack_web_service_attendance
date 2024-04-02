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
public class DocumentConfirm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_confirm_id")
    private Long id;
    @NotNull
    @Size(min = 2, max = 80)
    private String name;

    private byte[] documentScan;
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;

}
