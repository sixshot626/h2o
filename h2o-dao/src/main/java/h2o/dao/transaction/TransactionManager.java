package h2o.dao.transaction;


import h2o.dao.Dao;

public interface TransactionManager {

	void begin();
	
	void rollBack();
	
	void commit();

	Dao getDao();

}
