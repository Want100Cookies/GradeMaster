package com.datbois.grademaster.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "emailVerifyToken")
        }
)
public class User extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Email
    private String email;

    private String referenceId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean verified;

    @JsonIgnore
    private String emailVerifyToken;

    @JsonIgnore
    private String retardToken;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            joinColumns = @JoinColumn(name = "userId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roleId", referencedColumnName = "id")
    )
    private Set<Role> roles;

    public User() {
    }

    public User(String name, String email, String referenceId, String password, boolean verified, String emailVerifyToken, Set<Role> roles) {
        this.name = name;
        this.email = email;
        this.referenceId = referenceId;
        this.password = password;
        this.verified = verified;
        this.emailVerifyToken = emailVerifyToken;
        this.roles = roles;
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

    public String getPassword() {
        return password;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getEmailVerifyToken() {
        return emailVerifyToken;
    }

    public String getRetardToken() {
        return retardToken;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setEmailVerifyToken(String emailVerifyToken) {
        this.emailVerifyToken = emailVerifyToken;
    }

    public void setRetardToken(String retardToken) {
        this.retardToken = retardToken;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", referenceId='" + referenceId + '\'' +
                ", verified=" + verified +
                ", emailVerifyToken='" + emailVerifyToken + '\'' +
                ", roles=" + roles +
                '}';
    }
}
