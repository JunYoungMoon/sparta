package com.mjy.jpashop2;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
public class Member {
    @Id @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false)
    private String username;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

//    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    private LocalDate localDate;

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
