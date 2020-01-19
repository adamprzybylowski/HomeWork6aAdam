package com.infoshareacademy.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

import static java.util.stream.Collectors.joining;

@NamedQueries({
        @NamedQuery(
                name = "Address.findAll",
                query = "SELECT a FROM Address a"
        )
})
@Entity
@Table(name = "ADDRESSES")
public class Address {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement w MySQL
    private Long id;

    @Column(name = "street", length = 64)
    @NotNull
    private String street;

    @Column(name = "city", length = 64)
    @NotNull
    private String city;

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
    private List<Student> students;

    public Address() {
    }

    public Address(String street, String city) {
        this.street = street;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Address{");
        sb.append("id=").append(id);
        sb.append(", street='").append(street).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", students=").append(students
                .stream()
                .map(s -> s.getId().toString())
                .collect(joining(", ")));
        sb.append('}');
        return sb.toString();
    }
}
