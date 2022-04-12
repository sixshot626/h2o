package h2o.dao.impl.transaction;

import com.jenkov.db.scope.ScopingDataSource;
import h2o.dao.Dao;
import h2o.dao.Db;
import h2o.dao.DbUtil;
import h2o.dao.exception.DaoException;
import h2o.dao.transaction.ScopeManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;


public class ScopeManagerImpl implements ScopeManager {



	private final String dataSourceName;
	private final AtomicReference<DataSource> dataSourceRef = new AtomicReference<>();

	private volatile int transactionIsolationLevel = -1;

	public ScopeManagerImpl(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	@Override
	public void setTransactionIsolationLevel(Integer transactionIsolationLevel) {
		this.transactionIsolationLevel = transactionIsolationLevel;
	}

	@Override
	public String getDataSourceName() {
		return dataSourceName;
	}

	@Override
	public DataSource getDataSource() {

		DataSource dataSource = dataSourceRef.get();

		if (dataSource == null) {
			dataSource = DbUtil.getDataSource( this.dataSourceName);
			dataSourceRef.compareAndSet(null , DbUtil.getDataSource( this.dataSourceName));
		}

		return dataSource;
	}

	@Override
	public Connection openConnection() {
		try {
			Connection conn = this.getDataSource().getConnection();
			if ( transactionIsolationLevel > -1 ) {
				conn.setTransactionIsolation( transactionIsolationLevel );
			}
			return conn;
		} catch (SQLException e) {
			throw new DaoException(e);
		}

	}

	@Override
	public Object beginScope() {
		DataSource dataSource = this.getDataSource();
		if ( dataSource instanceof ScopingDataSource ) {
			((ScopingDataSource)dataSource).beginConnectionScope();
			return dataSource;
		}

		return null;
	}

	@Override
	public void endScope(Object dataSource) {
		if ( dataSource != null ) {
			((ScopingDataSource)dataSource).endConnectionScope();
		}
	}


}
