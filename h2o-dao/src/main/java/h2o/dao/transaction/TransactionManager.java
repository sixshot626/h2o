package h2o.dao.transaction;



public interface TransactionManager extends ScopeManager {

	Object beginTransaction();
	
	void rollBack(Object transactionObj , Throwable rootCause);
	
	void commit(Object transactionObj);

}
