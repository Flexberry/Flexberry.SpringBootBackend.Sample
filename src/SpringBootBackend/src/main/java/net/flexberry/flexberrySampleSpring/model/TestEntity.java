package net.flexberry.flexberrySampleSpring.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import java.util.UUID;

@Entity
@Audited
@Table(schema = "public", name = "testentity")
public class TestEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "primarykey")
    private UUID primarykey;

    @Column(name = "name")
    private String name;

    @Column(name = "points")
    private int points;

    @Column(name = "enabled")
    private boolean enabled;

    public TestEntity() {
        super();
    }

    public void setprimaryKey(UUID primarykey) {
        this.primarykey = primarykey;
    }

    public UUID getprimarykey() {
        return primarykey;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public int getpoints() {
        return points;
    }

    public void setpoints(int points) {
        this.points = points;
    }

    public boolean getenabled() {
        return enabled;
    }

    public void setenabled(boolean enabled) {
        this.enabled = enabled;
    }
}
