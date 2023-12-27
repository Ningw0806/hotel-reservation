package service;

import model.Customer;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Collection;

public class CustomerService {

    private static class LazyHolder {
        static final CustomerService INSTANCE = new CustomerService();
    }

    private final Map<String, Customer> customersByEmail;

    private CustomerService() {
        this.customersByEmail = new ConcurrentHashMap<>();
    }

    public static CustomerService getInstance() {
        return LazyHolder.INSTANCE;
    }

    public synchronized void addCustomer(final String email, final String firstName, final String lastName) {
        if (!customersByEmail.containsKey(email)) {
            Customer newCustomer = new Customer(firstName, lastName, email);
            customersByEmail.put(email, newCustomer);
        } else {
            System.out.println("Customer with email " + email + " already exists.");
        }
    }

    public Customer getCustomer(final String customerEmail) {
        return customersByEmail.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers() {
        return Collections.unmodifiableCollection(customersByEmail.values());
    }
}
