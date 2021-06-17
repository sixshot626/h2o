package h2o.common.thirdparty.spring.transaction;

import h2o.common.thirdparty.spring.factory.SpringFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Created by zhangjianwei on 2017/7/21.
 */
public class TransactionHelper {

    protected final TransactionTemplate transactionTemplate;


    public TransactionHelper() {
        this("transactionManager");
    }

    public TransactionHelper(String transactionManagerName ) {
        this.transactionTemplate = new TransactionTemplate(SpringFactory.<PlatformTransactionManager>getObject(transactionManagerName));
    }

    public TransactionHelper(PlatformTransactionManager transactionManager ) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public TransactionHelper(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }


    public TransactionHelper inNewTx() {
        transactionTemplate.setPropagationBehavior( TransactionDefinition.PROPAGATION_REQUIRES_NEW );
        return this;
    }

    public TransactionHelper readOnly() {
        transactionTemplate.setReadOnly(true);
        return this;
    }



    public <T> T executeAndReturn( TransactionCallback<T> action ) throws TransactionException {
        return transactionTemplate.execute(action);
    }


    public void execute( TransactionCallbackWithoutResult action ) throws TransactionException {
        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus status) {
                action.doInTransaction( status );
                return null;
            }
        });
    }



    public PlatformTransactionManager getTransactionManager() {
        return transactionTemplate.getTransactionManager();
    }




    public TransactionHelper setPropagationBehaviorName(String constantName) throws IllegalArgumentException {
        transactionTemplate.setPropagationBehaviorName(constantName);
        return this;
    }

    public TransactionHelper setPropagationBehavior(int propagationBehavior) {
        transactionTemplate.setPropagationBehavior(propagationBehavior);
        return this;
    }

    public int getPropagationBehavior() {
        return transactionTemplate.getPropagationBehavior();
    }

    public TransactionHelper setIsolationLevelName(String constantName) throws IllegalArgumentException {
        transactionTemplate.setIsolationLevelName(constantName);
        return this;
    }

    public TransactionHelper setIsolationLevel(int isolationLevel) {
        transactionTemplate.setIsolationLevel(isolationLevel);
        return this;
    }

    public int getIsolationLevel() {
        return transactionTemplate.getIsolationLevel();
    }

    public TransactionHelper setTimeout(int timeout) {
        transactionTemplate.setTimeout(timeout);
        return this;
    }

    public int getTimeout() {
        return transactionTemplate.getTimeout();
    }

    public TransactionHelper setReadOnly(boolean readOnly) {
        transactionTemplate.setReadOnly(readOnly);
        return this;
    }

    public boolean isReadOnly() {
        return transactionTemplate.isReadOnly();
    }

    public TransactionHelper setName(String name) {
        transactionTemplate.setName(name);
        return this;
    }

    public String getName() {
        return transactionTemplate.getName();
    }



    @Override
    public String toString() {
        return transactionTemplate.toString();
    }
}
