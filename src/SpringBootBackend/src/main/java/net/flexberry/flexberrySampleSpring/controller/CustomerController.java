package net.flexberry.flexberrySampleSpring.controller;

import net.flexberry.flexberrySampleSpring.db.filter.internal.Condition;
import net.flexberry.flexberrySampleSpring.model.Customer;
import net.flexberry.flexberrySampleSpring.service.CustomerService;
import net.flexberry.flexberrySampleSpring.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CustomerController {
    @Autowired
    CustomerService service;
    @Autowired
    KafkaProducerService kafkaProducerService;

    @GetMapping("/customers/{primarykey}")
    public Customer getCustomer(@PathVariable("primarykey") UUID primaryKey) {
        return service.getCustomer(primaryKey);
    }

    @GetMapping("/filteringCustomers")
    public List<Customer> getCommentsForPeriod(@RequestBody List<Condition> conditions) {
        return service.getFilteringCustomers(conditions);
    }

    @GetMapping("/customers")
    public List<Customer> getComments() {
        return service.getAllCustomers();
    }

    @DeleteMapping("/customers/{primaryKey}")
    public void deleteCustomer(@PathVariable("primaryKey") UUID primaryKey) {
        Customer customer = service.getCustomer(primaryKey);
        service.deleteCustomerByPrimaryKey(primaryKey);
        kafkaProducerService.sendObjectOperationToKafka("DELETE", customer);
    }

    @PostMapping("/customers")
    public Customer addCustomer(@RequestBody Customer customer) {
        Customer newCustomer = service.saveOrUpdateCustomer(customer);
        kafkaProducerService.sendObjectOperationToKafka("CREATE", newCustomer);
        return newCustomer;
    }

    @PutMapping("/customers")
    public Customer updateCustomer(@RequestBody Customer customer) {
        Customer newCustomer = service.saveOrUpdateCustomer(customer);
        kafkaProducerService.sendObjectOperationToKafka("UPDATE", newCustomer);
        return newCustomer;
    }
}
