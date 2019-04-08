package h2o.dao.impl.transaction;

import com.jenkov.db.scope.ScopingDataSource;
import h2o.dao.Dao;
import h2o.dao.ScopeManager;

import javax.sql.DataSource;


public class ScopeManagerImpl implements ScopeManager {


	@Override
	public Object beginScope(Dao dao) {

		DataSource dataSource = dao.getDataSource();
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
