package com.deshan.app.banking.ejb.bean;

import com.deshan.app.banking.model.Account;
import com.deshan.app.banking.model.AccountStatus;
import com.deshan.app.banking.service.AccountService;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.Collections;
import java.util.List;

@Stateless
public class AccountServiceBean implements AccountService {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void createAccount(Account account) {
        em.persist(account);
    }

    @Override
    public List<Account> getAccountsByCustomerId(Long customerId) {
        try {
            TypedQuery<Account> query = em.createNamedQuery("Account.findByCustomerId", Account.class);
            query.setParameter("customerId", customerId);
            return query.getResultList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Account getAccountByNumber(String accountNumber) {
        try {
            TypedQuery<Account> query = em.createNamedQuery("Account.findByAccountNumber", Account.class);
            query.setParameter("accountNumber", accountNumber);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Account getAccountById(Long id) {
        return em.find(Account.class, id);
    }

    @Override
    public void closeAccount(Long accountId) {
        Account account = em.find(Account.class, accountId);
        if (account != null) {
            account.setStatus(AccountStatus.CLOSED);
            em.merge(account);
        }
    }
}