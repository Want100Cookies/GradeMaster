package com.datbois.grademaster.model;

import javax.persistence.*;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"from_user_id", "to_user_id", "group_id"})
        }
)
public class Grade extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double grade;

    private String motivation;

    @ManyToOne(fetch = FetchType.LAZY)
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private User toUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private Group group;

    private boolean valid = true;

    public Grade() {

    }

    public Grade(Double grade, String motivation, User fromUser, User toUser, Group group) {
        this.grade = grade;
        this.motivation = motivation;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.group = group;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public void removeFromUser() {
        fromUser = null;
    }

    public void removeToUser() {
        toUser = null;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", grade=" + grade +
                ", motivation='" + motivation + '\'' +
                ", fromUser=" + fromUser +
                ", toUser=" + toUser +
                ", group=" + group +
                ", valid=" + valid +
                '}';
    }
}