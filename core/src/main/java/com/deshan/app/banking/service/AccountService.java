package com.deshan.app.banking.service;

import com.deshan.app.banking.model.Account;
import jakarta.ejb.Remote;
import java.util.List;

@Remote
public interface AccountService {

    void createAccount(Account account);

    List<Account> getAccountsByCustomerId(Long customerId);

    Account getAccountByNumber(String accountNumber);

    Account getAccountById(Long id);

    void closeAccount(Long accountId);
}