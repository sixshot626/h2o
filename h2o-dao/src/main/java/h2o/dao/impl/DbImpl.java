package h2o.dao.impl;

import h2o.dao.Dao;
import h2o.dao.Db;
import h2o.dao.DbUtil;
import h2o.dao.page.PagingProcessor;
import h2o.dao.transaction.ScopeManager;

import java.sql.Connection;
import java.util.Optional;


public class DbImpl extends AbstractDb implements Db {


	public DbImpl(ScopeManager scopeManager) {
		super(scopeManager);
	}

	@Override
	public Dao getDao() {
		return createDao(this.getScopeManager().openConnection());
	}

	@Override
	public Dao createDao(Connection connection) {

		DaoImpl daoImpl = new DaoImpl( connection );

		daoImpl.setArgProcessor( DbUtil.DBFACTORY.getArgProcessor() );
		daoImpl.setOrmProcessor( DbUtil.DBFACTORY.getOrmProcessor() );

		Optional<PagingProcessor> pagingProcessor = DbUtil.DBFACTORY.getPagingProcessor( this.getScopeManager().getDataSourceName());
		if ( pagingProcessor.isPresent() ) {
			daoImpl.setPagingProcessor( pagingProcessor.get() );
		}

		return daoImpl;
	}

}
