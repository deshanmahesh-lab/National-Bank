package com.deshan.app.banking.ejb.bean;

import com.deshan.app.banking.model.ScheduledTransaction;
import com.deshan.app.banking.service.ScheduledTransactionService;
import com.deshan.app.banking.service.TransactionService;
import jakarta.annotation.Resource;
import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class ScheduledTransactionServiceBean implements ScheduledTransactionService {

    private static final Logger LOGGER = Logger.getLogger(ScheduledTransactionServiceBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Resource
    private TimerService timerService;

    @EJB
    private TransactionService transactionService;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void scheduleTransfer(ScheduledTransaction scheduledTransaction) {
        em.persist(scheduledTransaction);
        em.flush();

        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setInfo(scheduledTransaction.getId());
        timerConfig.setPersistent(true);

        timerService.createSingleActionTimer(scheduledTransaction.getScheduledTimestamp(), timerConfig);
    }

    @Timeout
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void executeScheduledTransfer(Timer timer) {
        Long scheduledTransactionId = (Long) timer.getInfo();
        ScheduledTransaction scheduledTx = em.find(ScheduledTransaction.class, scheduledTransactionId);

        if (scheduledTx != null && "PENDING".equals(scheduledTx.getStatus())) {
            try {
                transactionService.transferFunds(
                        scheduledTx.getSourceAccount().getId(),
                        scheduledTx.getDestinationAccountNumber(),
                        scheduledTx.getAmount()
                );
                scheduledTx.setStatus("COMPLETED");
            } catch (Exception e) {
                scheduledTx.setStatus("FAILED");
                LOGGER.log(Level.SEVERE, "Scheduled fund transfer FAILED for ID: {0}. Reason: {1}",
                        new Object[]{scheduledTransactionId, e.getMessage()});
            }
            em.merge(scheduledTx);
        }
    }

    @Override
    public List<ScheduledTransaction> getScheduledTransactionsByCustomerId(Long customerId) {
        try {
            TypedQuery<ScheduledTransaction> query = em.createQuery(
                    "SELECT st FROM ScheduledTransaction st JOIN FETCH st.sourceAccount WHERE st.sourceAccount.customer.id = :customerId ORDER BY st.scheduledTimestamp DESC",
                    ScheduledTransaction.class
            );
            query.setParameter("customerId", customerId);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching scheduled transactions for customer ID: " + customerId, e);
            return Collections.emptyList();
        }
    }
}