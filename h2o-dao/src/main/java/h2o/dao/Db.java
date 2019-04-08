package h2o.dao;


public interface Db {


    void setTransactionManager(TransactionManager txManager);
    void setScopeManager(ScopeManager scopeManager);


    Dao getDao();
	
	Dao getDao(boolean autoClose);

	<T> T q(DaoCallback<T> txCallback);
	
	<T> T tx(DaoCallback<T> txCallback);

}
