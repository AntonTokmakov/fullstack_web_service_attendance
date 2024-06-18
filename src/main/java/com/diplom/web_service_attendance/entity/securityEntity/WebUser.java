package com.diplom.web_service_attendance.entity.securityEntity;

import com.diplom.web_service_attendance.entity.StudyGroup;
import com.diplom.web_service_attendance.entity.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "s_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebUser  {
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

    @ManyToOne
    @JoinColumn(name = "study_group_id")
    private StudyGroup studyGroup;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;


    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "s_user_authority",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private List<Authority> authorities;


    public void addAuthority(Authority authority) {
        if (this.authorities == null) {
            this.authorities = new ArrayList<>();
        }
        this.authorities.add(authority);
    }
}
