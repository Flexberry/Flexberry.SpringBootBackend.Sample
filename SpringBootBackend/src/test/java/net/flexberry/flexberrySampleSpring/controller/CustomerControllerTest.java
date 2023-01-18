package net.flexberry.flexberrySampleSpring.controller;

import net.flexberry.flexberrySampleSpring.model.Comment;
import net.flexberry.flexberrySampleSpring.model.Customer;
import net.flexberry.flexberrySampleSpring.repository.CustomerRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    CustomerRepository repository;

    private String getEndpoint(String id) {
        return "http://localhost:" + port + "/api/customers" + (id == null ? "" : "/" + id);
    }

    private String getEndpoint() {
        return getEndpoint(null);
    }

    private Customer createCustomer(String name, Integer age, UUID primaryKey, Comment comment) {
        var customer = new Customer();

        customer.setName(name);
        customer.setAge(age);

        if (primaryKey != null) {
            customer.setPrimarykey(primaryKey);
        }

        if (comment != null) {
            comment.setCustomer(customer);
            customer.setComments(List.of(comment));
        }

        return customer;
    }

    private Customer createCustomer(String name, Integer age) {
        return createCustomer(name, age, UUID.randomUUID(), null);
    }

    private Customer createCustomer(String name, Integer age, UUID primaryKey) {
        return createCustomer(name, age, primaryKey, null);
    }

    private Customer createCustomer(String name, Integer age, Comment comment) {
        return createCustomer(name, age, UUID.randomUUID(), comment);
    }

    @Test
    void getCustomer() throws NullPointerException {
        Integer customerAge = 1;
        String customerName = "New Customer";
        String commentText = "I was born.";
        Date commentDate = new Date();

        var comment = new Comment();
        comment.setPrimarykey(UUID.randomUUID());
        comment.setCommentText(commentText);
        comment.setCommentDate(commentDate);

        UUID customerId = repository.save(createCustomer(customerName, customerAge, comment)).getPrimarykey();

        ResponseEntity<Customer> response = restTemplate.getForEntity(getEndpoint(customerId.toString()), Customer.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getAge()).isEqualTo(customerAge);
        assertThat(response.getBody().getName()).isEqualTo(customerName);
        assertThat(response.getBody().getComments().size()).isEqualTo(1);
        assertThat(response.getBody().getComments().get(0).getCommentText()).isEqualTo(commentText);
        assertThat(response.getBody().getComments().get(0).getCommentDate()).hasSameTimeAs(commentDate);
    }

    @Test
    void deleteCustomer() {
        Customer customer = repository.save(createCustomer("New Customer", 1));

        assertThat(
            restTemplate.exchange(
                getEndpoint(customer.getPrimarykey().toString()),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Object.class
            ).getStatusCode()
        ).isEqualTo(HttpStatus.OK);

        assertThat(repository.findById(customer.getPrimarykey()).orElse(null)).isNull();
    }

    @Test
    void addCustomer() {
        UUID customerId = UUID.randomUUID();

        Customer customer = createCustomer("New Customer", 1, customerId);

        assertThat(
            restTemplate.postForEntity(
                getEndpoint(),
                customer,
                String.class
            ).getStatusCode()
        ).isEqualTo(HttpStatus.OK);

        assertThat(repository.findById(customerId).orElse(null))
            .usingRecursiveComparison()
            .ignoringFields("comments")
            .isEqualTo(customer);
    }

    @Test
    void updateCustomer() {
        Customer customer = repository.save(createCustomer("New Customer", 1));

        Integer customerAge = 99;
        String customerName = "Old Customer";

        customer.setAge(customerAge);
        customer.setName(customerName);

        assertThat(
            restTemplate.exchange(
                getEndpoint(),
                HttpMethod.PUT,
                new HttpEntity<Customer>(customer),
                String.class
            ).getStatusCode()
        ).isEqualTo(HttpStatus.OK);

        Customer updatedCustomer = repository.findById(customer.getPrimarykey()).orElse(new Customer());

        assertThat(updatedCustomer.getAge()).isEqualTo(customerAge);
        assertThat(updatedCustomer.getName()).isEqualTo(customerName);
    }
}