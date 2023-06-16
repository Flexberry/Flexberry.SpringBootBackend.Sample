package net.flexberry.flexberrySampleSpring.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import net.flexberry.flexberrySampleSpring.db.filter.internal.Condition;
import net.flexberry.flexberrySampleSpring.model.Customer;
import net.flexberry.flexberrySampleSpring.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CustomerController {
    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @Operation(summary = "Get customer by primary key")
    @GetMapping("/customers/{primarykey}")
    public Customer getCustomer(@PathVariable("primarykey") UUID primaryKey) {
        return service.getCustomer(primaryKey);
    }

    @Operation(summary = "Get customer by custom filters")
    @GetMapping("/filteringCustomers")
    @Parameter(
            description ="""
            JSON for example 
            [
                {
                    "dataType": "string",
                    "compareType": "eq",
                    "value": "Vasia",
                    "field": "name"
                }
                {
                    "dataType": "numeric",
                    "compareType": "eq",
                    "value": 31,
                    "field": "age"
                }
            ]
            """,
            name = "conditions")
    public List<Customer> getCommentsForPeriod(@RequestBody List<Condition> conditions) {
        return service.getFilteringCustomers(conditions);
    }

    @Operation(summary = "Get all customers")
    @GetMapping("/customers")
    public List<Customer> getComments() {
        return service.getAllCustomers();
    }

    @Operation(summary = "Delete customer with primary key")
    @DeleteMapping("/customers/{primaryKey}")
    public void deleteCustomer(@PathVariable("primaryKey") UUID primaryKey) {
        service.deleteCustomerByPrimaryKey(primaryKey);
    }

    @Operation(summary = "Post customer")
    @PostMapping("/customers")
    public Customer addCustomer(@RequestBody Customer customer) {
        return service.saveOrUpdateCustomer(customer);
    }

    @Operation(summary = "Update customer")
    @PutMapping("/customers")
    public Customer updateCustomer(@RequestBody Customer customer) {
        return service.saveOrUpdateCustomer(customer);
    }
}
