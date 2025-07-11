package service;

import model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerService {
    // Static variable reference of instance
    // of type CustomerService
    private static CustomerService instance = null;

    public CustomerService() {
        this.customers = new HashMap<>();
    }

    // Declaring a variable of type Map<String,Customer>
    public Map<String, Customer> customers;

    // Static method
    // Static method to create instance of CustomerService class
    public static CustomerService getInstance() {
        if (instance == null) {
            instance = new CustomerService();
        }
        return instance;
    }


    public void addCustomer(String email, String firstName, String lastName) {
        Customer customer = new Customer(firstName, lastName, email);
        customers.put(email, customer);
    }
    public Customer getCustomer(String customerEmail) {
        if(customers.containsKey(customerEmail)) {
            return customers.get(customerEmail);
        }
        return null;
    }
    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }
}
