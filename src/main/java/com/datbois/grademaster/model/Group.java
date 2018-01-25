package com.datbois.grademaster.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Group extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer startYear;
    private Integer endYear;

    @ElementCollection(targetClass = Period.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "group_periods")
    private Set<Period> period;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToMany(mappedBy = "groups", fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private Set<User> users;

    @OneToOne(fetch = FetchType.LAZY)
    private GroupGrade groupGrade;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
    private List<Grade> grades;

    private String groupName;

    public Group() {
    }

    public Group(int startYear, int endYear, Set<Period> period, Course course, String groupName, Set<User> users) {
        this.startYear = startYear;
        this.endYear = endYear;
        this.period = period;
        this.course = course;
        this.groupName = groupName;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public Set<Period> getPeriod() {
        return period;
    }

    public void setPeriod(Set<Period> period) {
        this.period = period;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public GroupGrade getGroupGrade() {
        return groupGrade;
    }

    public void setGroupGrade(GroupGrade groupGrade) {
        this.groupGrade = groupGrade;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public Integer getGradingProgress() {
        if (getGroupGrade() == null) {
            return 0;
        }

        Integer gradesFromTeachers = (int) getGrades()
                .stream()
                .filter(grade -> grade.getFromUser().hasAnyRole("TEACHER_ROLE"))
                .count();

        if (gradesFromTeachers > 0) {
            return 100;
        }

        if (getGrades().size() == 0) {
            return 10;
        }

        Integer gradesFromStudents = getGrades()
                .stream()
                .filter(grade -> grade.getFromUser().hasAnyRole("STUDENT_ROLE"))
                .toArray()
                .length;

        Double noStudents = (double) getUsers()
                .stream()
                .filter(user -> user.hasAnyRole("STUDENT_ROLE"))
                .toArray()
                .length;

        Double studentsThatGraded = gradesFromStudents / noStudents;

        return (int) (studentsThatGraded / noStudents * 100);
    }

    @JsonIgnore
    public Status getGradingStatusForUser(User user) {
        Status status;

        if (user.hasAnyRole("STUDENT_ROLE")) {
            // No group grade has been given
            status = Status.INACTIVE;

            if (getGroupGrade() != null) {
                if (getGrades()
                        .stream()
                        .filter(grade -> grade.getToUser().getId().equals(user.getId()) && grade.getFromUser().hasAnyRole("TEACHER_ROLE"))
                        .findFirst()
                        .orElse(null) != null
                        ) {
                    // The student has received the final grade from the teacher
                    status = Status.CLOSED;
                } else if (getGroupGrade().getDeadline().isBeforeNow()) {
                    status = Status.PENDING;
                } else if (getGrades()
                        .stream()
                        .noneMatch(grade -> grade.getFromUser().getId().equals(user.getId()))) {
                    // Group grade has been given
                    // And the student hasn't graded anyone
                    status = Status.OPEN;
                } else {
                    // Waiting for the teacher to finalize the grade
                    status = Status.PENDING;
                }
            }
        } else {
            status = Status.OPEN;

            if (getGroupGrade() != null) {
                status = Status.PENDING;

                if (getGrades()
                        .stream()
                        .filter(grade -> grade.getFromUser().hasAnyRole("TEACHER_ROLE"))
                        .count()
                        ==
                        getUsers()
                                .stream()
                                .filter(groupUser -> groupUser.hasAnyRole("STUDENT_ROLE"))
                                .count()
                        ) {
                    status = Status.CLOSED;
                }
            }
        }

        return status;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", startYear=" + startYear +
                ", endYear=" + endYear +
                ", period=" + period +
                ", course='" + course + '\'' +
                ", groupName='" + groupName + '\'' +
                ", users='" + users +
                '}';
    }
}
