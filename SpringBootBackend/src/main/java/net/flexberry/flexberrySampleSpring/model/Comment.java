package net.flexberry.flexberrySampleSpring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;

import java.util.Date;
import java.util.UUID;

@Entity
@Audited
@Table(schema = "public", name = "comment")
public class Comment {
    @Id
    private UUID primarykey;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "commentdate")
    private Date commentDate;

    @Column(name = "commenttext")
    private String commentText;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnoreProperties("comments")
    private Customer customer;

    public Comment() {
        super();
    }
    public UUID getPrimarykey() {
        return primarykey;
    }
    public void setPrimarykey(UUID primarykey) {
        this.primarykey = primarykey;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
