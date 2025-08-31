package com.djeno.lab1.services;

import lombok.RequiredArgsConstructor;
import org.hibernate.TransactionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final JtaTransactionManager jtaTransactionManager;

    public TransactionStatus begin(String transactionName, int timeoutSec) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName(transactionName);
        def.setTimeout(timeoutSec);
        def.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED);
        return jtaTransactionManager.getTransaction(def);
    }

    public void commit(TransactionStatus status) {
        if (!status.isCompleted()) {
            jtaTransactionManager.commit(status);
        }
    }

    public void rollback(TransactionStatus status) {
        if (!status.isCompleted()) {
            jtaTransactionManager.rollback(status);
        }
    }

    public <T> T execute(String txName, int timeoutSec, TransactionCallback<T> action) {
        TransactionStatus status = null;
        try {
            status = begin(txName, timeoutSec);
            T result = action.doInTransaction(status);
            commit(status);
            return result;
        } catch (Exception e) {
            if (status != null) {
                rollback(status);
            }
            throw new TransactionException("Transaction failed: " + txName, e);
        }
    }
}
