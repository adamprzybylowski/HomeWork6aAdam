package com.infoshareacademy.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "STUDENTS")
@NamedQueries({
    @NamedQuery(
        name = "Student.findBornAfter",
        query = "SELECT s FROM Student s WHERE s.dateOfBirth >= :param1 "
            + "ORDER BY s.dateOfBirth DESC"
    ),
    @NamedQuery(
        name = "Student.findAll",
        query = "SELECT s FROM Student s"
    )
})
public class Student {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  @NotNull
  private String name;

  @Column(name = "last_name")
  @NotNull
  private String surname;

  @Column(name = "date_of_birth")
  @NotNull
  private LocalDate dateOfBirth;

  @OneToOne
  @JoinColumn(name = "computer_id", unique = true)
  private Computer computer;

  @ManyToOne
  @JoinColumn(name = "address_id")
  private Address address;

  @ManyToMany
  @JoinTable(name = "STUDENTS_TO_COURSES",
      joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"), // STUDENTS
      inverseJoinColumns = @JoinColumn(name = "course_name", referencedColumnName = "name"))
// COURSES
  private List<Course> courses;

  public Student() {

  }

  public Student(String name, String surname, LocalDate dateOfBirth, Computer computer,
                 Address address, List<Course> courses) {
    this.name = name;
    this.surname = surname;
    this.dateOfBirth = dateOfBirth;
    this.computer = computer;
    this.address = address;
    this.courses = courses;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public Computer getComputer() {
    return computer;
  }

  public void setComputer(Computer computer) {
    this.computer = computer;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public List<Course> getCourses() {
    return courses;
  }

  public void setCourses(List<Course> courses) {
    this.courses = courses;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("Student{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", surname='").append(surname).append('\'');
    sb.append(", dateOfBirth=").append(dateOfBirth);
    sb.append(", computer=").append(computer);
    sb.append(", address=").append(address);
    sb.append(", courses=").append(courses);
    sb.append('}');
    return sb.toString();
  }
}
