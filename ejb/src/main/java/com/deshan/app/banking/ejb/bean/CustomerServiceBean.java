package com.deshan.app.banking.ejb.bean;

import com.deshan.app.banking.model.AccountStatus;
import com.deshan.app.banking.model.Customer;
import com.deshan.app.banking.service.CustomerService;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.Collections;
import java.util.List;

@Stateless
public class CustomerServiceBean implements CustomerService {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void addCustomer(Customer customer) {
        em.persist(customer);
    }

    @Override
    public void updateCustomer(Customer customer) {
        em.merge(customer);
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        try {
            TypedQuery<Customer> query = em.createNamedQuery("Customer.findByEmail", Customer.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Customer getCustomerById(Long id) {
        return em.find(Customer.class, id);
    }

    @Override
    public boolean validateCredentials(String email, String encryptedPassword) {
        Customer customer = getCustomerByEmail(email);
        return customer != null
                && customer.getPassword() != null
                && customer.getPassword().equals(encryptedPassword)
                && customer.getStatus() == AccountStatus.ACTIVE;
    }

    @Override
    public List<Customer> getAllCustomers() {
        try {
            TypedQuery<Customer> query = em.createQuery("SELECT c FROM Customer c", Customer.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}