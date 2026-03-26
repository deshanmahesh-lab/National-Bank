package com.deshan.app.banking.service;

import com.deshan.app.banking.exception.InsufficientFundsException;
import com.deshan.app.banking.model.Transaction;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface TransactionService {

    void transferFunds(Long fromAccountId, String toAccountNumber, double amount) throws InsufficientFundsException;

    List<Transaction> getTransactionHistory(Long accountId);

    void adminDeposit(Long toAccountId, double amount);

}