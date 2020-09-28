package h2o.dao;


import h2o.dao.transaction.ScopeManager;
import h2o.dao.transaction.TransactionManager;

public interface Db {


    void setTransactionManager(TransactionManager txManager);
    void setScopeManager(ScopeManager scopeManager);


    Dao getDao();
	
	Dao getDao(boolean autoClose);

	<T> T q(DaoCallback<T> txCallback);
	
	<T> T tx(DaoCallback<T> txCallback);

}
