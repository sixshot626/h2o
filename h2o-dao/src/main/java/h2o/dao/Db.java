package h2o.dao;


import h2o.dao.transaction.ScopeManager;
import h2o.dao.transaction.TransactionManager;

import javax.sql.DataSource;
import java.sql.Connection;

public interface Db {

    Dao getDao();
    Dao createDao(Connection connection);

    ScopeManager getScopeManager();


	<T> T q(DaoCallback<T> txCallback);
	<T> T tx(DaoCallback<T> txCallback);

}
