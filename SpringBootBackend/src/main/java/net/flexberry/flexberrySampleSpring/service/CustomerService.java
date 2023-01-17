package net.flexberry.flexberrySampleSpring.service;

import net.flexberry.flexberrySampleSpring.db.filter.CustomFilter;
import net.flexberry.flexberrySampleSpring.db.filter.internal.Condition;
import net.flexberry.flexberrySampleSpring.model.Customer;
import net.flexberry.flexberrySampleSpring.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository repository;

    public Customer getCustomer(UUID primarykey) {
        return repository.findById(primarykey).orElse(null);
    }

    public Customer saveOrUpdateCustomer(Customer customer) {
        return repository.save(customer);
    }

    public void deleteCustomerByPrimaryKey(UUID primarykey) {
        repository.deleteById(primarykey);
    }

    public List<Customer> getFilteringCustomers(List<Condition> conditions) {
        CustomFilter filter = new CustomFilter(conditions);
        List<Customer> customers = repository.findAll(filter);

        return customers;
    }
}
