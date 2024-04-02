package com.diplom.web_service_attendance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
public class ParityOfWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parity_week_id")
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    public enum ParityWeekEnum {
        ЧЕТНАЯ,
        НЕЧЕТНАЯ
    }

}
