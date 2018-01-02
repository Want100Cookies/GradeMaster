package com.datbois.grademaster.model;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String referenceId;
    private String password;

    @ManyToOne
    private Role role;

    public User() {
    }

    public User(String name, String email, String referenceId, String password, Role role) {
        this.name = name;
        this.email = email;
        this.referenceId = referenceId;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", referenceId='" + referenceId + '\'' +
                ", role=" + role +
                '}';
    }
}
