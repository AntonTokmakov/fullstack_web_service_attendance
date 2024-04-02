package com.diplom.web_service_attendance.entity.securityEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "s_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(unique = true)
    private String username;

    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @NotNull
    @Size(min = 2, max = 10)
    private String studyGroupShortName;

    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "s_user_authority",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private List<Authority> authorities;
}
