package h2o.dao.transaction;


import h2o.dao.Dao;

public interface TransactionManager {

	void begin();
	
	void rollback();
	
	void commit();

	Dao getDao();

}
