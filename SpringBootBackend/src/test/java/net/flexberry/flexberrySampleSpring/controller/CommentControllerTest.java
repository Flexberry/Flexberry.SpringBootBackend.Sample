package net.flexberry.flexberrySampleSpring.controller;

import net.flexberry.flexberrySampleSpring.model.Comment;
import net.flexberry.flexberrySampleSpring.model.Customer;
import net.flexberry.flexberrySampleSpring.repository.CommentRepository;
import net.flexberry.flexberrySampleSpring.repository.CustomerRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private CommentRepository commentRepository;

    private String getEndpoint(String id) {
        return "http://localhost:" + port + "/api/comments" + (id == null ? "" : "/" + id);
    }

    private String getEndpoint() {
        return getEndpoint(null);
    }

    private Comment createComment(String text, Date date, UUID primaryKey) {
        var comment = new Comment();

        if (primaryKey != null) {
            comment.setPrimarykey(primaryKey);
        }

        comment.setCommentText(text);
        comment.setCommentDate(date);

        var customer = new Customer();

        customer.setPrimarykey(UUID.randomUUID());
        customer.setName("New Customer");
        customer.setAge(1);
        customer.setComments(List.of(comment));

        comment.setCustomer(customer);

        return comment;
    }

    private Comment createComment(String text, Date date) {
        return createComment(text, date, UUID.randomUUID());
    }

    @Test
    void getComment() {
        String commentText = "I was born.";
        Date commentDate = new Date();

        Comment comment = repository.save(createComment(commentText, commentDate).getCustomer()).getComments().get(0);

        ResponseEntity<Comment> response = restTemplate.getForEntity(getEndpoint(comment.getPrimarykey().toString()), Comment.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getCommentText()).isEqualTo(commentText);
        assertThat(response.getBody().getCommentDate()).hasSameTimeAs(commentDate);
    }

    @Test
    void deleteComment() {
        String commentText = "I was born.";
        Date commentDate = new Date();

        Comment comment = repository.save(createComment(commentText, commentDate).getCustomer()).getComments().get(0);

        assertThat(
            restTemplate.exchange(
                getEndpoint(comment.getPrimarykey().toString()),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Object.class
            ).getStatusCode()
        ).isEqualTo(HttpStatus.OK);

        assertThat(repository.findById(comment.getPrimarykey()).orElse(null)).isNull();
    }

    @Test
    void addComment() {
        UUID commentId = UUID.randomUUID();
        String commentText = "I was born.";
        Date commentDate = new Date();

        Comment comment = createComment(commentText, commentDate, commentId);

        comment.getCustomer().setComments(List.of());
        repository.save(comment.getCustomer());

        assertThat(
            restTemplate.postForEntity(
                getEndpoint(),
                comment,
                String.class
            ).getStatusCode()
        ).isEqualTo(HttpStatus.OK);

        assertThat(commentRepository.findById(commentId).orElse(null))
            .usingRecursiveComparison()
            .ignoringFields("customer.comments")
            .withEqualsForType((d1, d2) -> d1.getTime() == d2.getTime(), Date.class)
            .isEqualTo(comment);
    }

    @Test
    void updateComment() {
        String commentText = "I was born.";
        Date commentDate = new Date(0);

        Comment comment = repository.save(createComment("", new Date()).getCustomer()).getComments().get(0);

        comment.setCommentText(commentText);
        comment.setCommentDate(commentDate);

        assertThat(
            restTemplate.exchange(
                getEndpoint(),
                HttpMethod.PUT,
                new HttpEntity<Comment>(comment),
                String.class
            ).getStatusCode()
        ).isEqualTo(HttpStatus.OK);

        Comment updatedComment = commentRepository.findById(comment.getPrimarykey()).orElse(new Comment());

        assertThat(updatedComment.getCommentText()).isEqualTo(commentText);
        assertThat(updatedComment.getCommentDate()).hasSameTimeAs(commentDate);
    }
}