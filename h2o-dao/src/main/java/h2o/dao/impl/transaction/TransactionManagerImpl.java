package h2o.dao.impl.transaction;


import com.jenkov.db.scope.ScopingDataSource;
import h2o.dao.Dao;
import h2o.dao.Db;
import h2o.dao.transaction.TransactionManager;

import javax.sql.DataSource;

public class TransactionManagerImpl extends ScopeManagerImpl implements TransactionManager {


	public TransactionManagerImpl(String dataSourceName) {
		super(dataSourceName);
	}

	@Override
	public Object beginTransaction() {

		DataSource dataSource = getDataSource();
		if ( dataSource instanceof ScopingDataSource) {
			((ScopingDataSource)dataSource).beginTransactionScope();
			return dataSource;
		}

		return null;
	}

	@Override
	public void rollBack(Object dataSource , Throwable rootCause ) {

		if ( dataSource != null ) {
			((ScopingDataSource)dataSource).abortTransactionScope(rootCause);
		}

	}

	@Override
	public void commit(Object dataSource) {

		if ( dataSource != null ) {
			((ScopingDataSource)dataSource).endTransactionScope();
		}

	}


}
