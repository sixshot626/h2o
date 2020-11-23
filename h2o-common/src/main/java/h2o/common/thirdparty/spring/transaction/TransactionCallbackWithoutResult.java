package h2o.common.thirdparty.spring.transaction;

import org.springframework.transaction.TransactionStatus;

public interface TransactionCallbackWithoutResult {

    void doInTransaction(TransactionStatus status);

}
