package h2o.dao.transaction;

import h2o.dao.Dao;

public interface TransactionManager {
	
	Object beginTransaction(Dao dao);
	
	void rollBack(Object transactionObj , Throwable rootCause);
	
	void commit(Object transactionObj);

}
