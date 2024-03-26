package com.io.threegonew.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;

@Table(name="users")
@NoArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User /*implements UserDetails*/ {

    @Id
    @Column(name = "u_id", updatable = false, unique = true)
    private String id;

    @Column(name = "u_pw")
    private String pw;

    @Column(name = "u_name")
    private String name;

    @Column(name = "u_email", nullable = false, unique = true)
    private String email;

    @Column(name = "u_ofile")
    private String u_ofile;

    @Column(name = "u_sfile")
    private String u_sfile;

    @Column(name = "u_about")
    private String about;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Team> sharedPlanners = new HashSet<>();

    @Builder
    public User(String id, String pw, String name, String email, String u_ofile, String u_sfile, String about){
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.email = email;
        this.u_ofile = u_ofile;
        this.u_sfile = u_sfile;
        this.about = about;
    }

    public void update(String name, String about/*, String u_ofile, String u_sfile*/){
        this.name = name;
        this.about = about;
    }



}
