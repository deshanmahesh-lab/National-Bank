package com.deshan.app.banking.service;

import com.deshan.app.banking.model.ScheduledTransaction;
import jakarta.ejb.Remote;
import java.util.List;

@Remote
public interface ScheduledTransactionService {

    void scheduleTransfer(ScheduledTransaction scheduledTransaction);

    List<ScheduledTransaction> getScheduledTransactionsByCustomerId(Long customerId);

}