package net.flexberry.flexberrySampleSpring.model;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;

import java.util.List;
import java.util.UUID;

@Entity
@Audited
@Table(schema = "public", name = "customer")
public class Customer {
    @Id
    @Column(name = "primarykey")
    private UUID primarykey;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private List<Comment> comments;

    public Customer() {
        super();
    }

    public UUID getPrimarykey() {
        return primarykey;
    }

    public void setPrimarykey(UUID primarykey) {
        this.primarykey = primarykey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
