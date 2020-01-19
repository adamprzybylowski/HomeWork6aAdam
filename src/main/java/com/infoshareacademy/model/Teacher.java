package com.infoshareacademy.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "TEACHER")
@NamedQueries({@NamedQuery(name = "techaer.findAll",
        query = "SELECT t FROM Teacher t")})
public class Teacher {


    @Id
    @Column(name = "pesel", length = 11)
    @NotNull
    private String pesel;


    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "last_name")
    @NotNull
    private String surname;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TEACHER_TO_COURSES",
            joinColumns = @JoinColumn(name = "teacher_pesel", referencedColumnName = "pesel"), // Teachers
            inverseJoinColumns = @JoinColumn(name = "course_name", referencedColumnName = "name"))
// COURSES
    private List<Course> courses = new ArrayList<>();

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    public Teacher() {

    }

    public Teacher(String name, String surname, String pesel,
                   List<Course> courses) {
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
        this.courses = courses;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Teacher{");
        sb.append("pesel=").append(pesel);
        sb.append(", name='").append(name).append('\'');
        sb.append(", surname='").append(surname).append('\'');
        sb.append(", courses=").append(courses);
        sb.append('}');
        return sb.toString();
    }

}
