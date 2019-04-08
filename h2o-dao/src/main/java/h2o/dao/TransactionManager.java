package h2o.dao;

public interface TransactionManager {
	
	Object beginTransaction(Dao dao);
	
	void rollBack(Object transactionObj , Throwable rootCause);
	
	void commit(Object transactionObj);

}
