package com.deshan.app.banking.ejb.bean;

import com.deshan.app.banking.exception.InsufficientFundsException;
import com.deshan.app.banking.model.Account;
import com.deshan.app.banking.model.Transaction;
import com.deshan.app.banking.service.TransactionService;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Status;
import jakarta.transaction.UserTransaction;

import java.util.List;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class TransactionServiceBean implements TransactionService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    @Override
    public void transferFunds(Long fromAccountId, String toAccountNumber, double amount) throws InsufficientFundsException {
        try {
            utx.begin();
            em.joinTransaction();

            Account fromAccount = em.find(Account.class, fromAccountId);
            if (fromAccount.getBalance() < amount) {
                throw new InsufficientFundsException("Insufficient funds for transfer.");
            }

            TypedQuery<Account> query = em.createNamedQuery("Account.findByAccountNumber", Account.class);
            query.setParameter("accountNumber", toAccountNumber);
            Account toAccount = query.getSingleResult();

            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);

            Transaction debitTransaction = new Transaction("DEBIT", amount, "Transfer to " + toAccountNumber, fromAccount, toAccount);
            Transaction creditTransaction = new Transaction("CREDIT", amount, "Transfer from " + fromAccount.getAccountNumber(), toAccount, fromAccount);

            em.merge(fromAccount);
            em.merge(toAccount);
            em.persist(debitTransaction);
            em.persist(creditTransaction);

            utx.commit();
        } catch (InsufficientFundsException e) {
            rollbackTransaction();
            throw e;
        } catch (Exception e) {
            rollbackTransaction();
            throw new RuntimeException("Fund transfer failed due to an unexpected error.", e);
        }
    }

    @Override
    public List<Transaction> getTransactionHistory(Long accountId) {
        try {
            utx.begin();
            List<Transaction> transactions = em.createNamedQuery("Transaction.findByAccountId", Transaction.class)
                    .setParameter("accountId", accountId)
                    .getResultList();
            utx.commit();
            return transactions;
        } catch (Exception e) {
            rollbackTransaction();
            throw new RuntimeException("Failed to retrieve transaction history.", e);
        }
    }

    @Override
    public void adminDeposit(Long toAccountId, double amount) {
        try {
            utx.begin();
            em.joinTransaction();

            Account toAccount = em.find(Account.class, toAccountId);
            toAccount.setBalance(toAccount.getBalance() + amount);

            Transaction creditTransaction = new Transaction("DEPOSIT", amount, "Deposit by administrator", toAccount, null);

            em.merge(toAccount);
            em.persist(creditTransaction);

            utx.commit();
        } catch (Exception e) {
            rollbackTransaction();
            throw new RuntimeException("Admin deposit failed.", e);
        }
    }

    private void rollbackTransaction() {
        try {
            if (utx.getStatus() == Status.STATUS_ACTIVE || utx.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
                utx.rollback();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to rollback transaction.", e);
        }
    }
}