package com.deshan.app.banking.service;

import com.deshan.app.banking.model.Customer;
import jakarta.ejb.Remote;
import java.util.List;

@Remote
public interface CustomerService {

    void addCustomer(Customer customer);

    void updateCustomer(Customer customer);

    Customer getCustomerByEmail(String email);

    Customer getCustomerById(Long id);

    boolean validateCredentials(String email, String encryptedPassword);

    List<Customer> getAllCustomers();
}