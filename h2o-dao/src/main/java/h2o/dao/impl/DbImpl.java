package h2o.dao.impl;

import h2o.common.dao.butterflydb.ButterflyDb;
import h2o.dao.Dao;
import h2o.dao.Db;
import h2o.dao.DbUtil;
import h2o.dao.page.PagingProcessor;

import javax.sql.DataSource;
import java.util.Optional;


public class DbImpl extends AbstractDb implements Db {

	private final String dataSourceName;

	private final DataSource dataSource;

	public DbImpl(String dataSourceName) {
		this.dataSourceName = dataSourceName;
		this.dataSource = DbUtil.getDataSource(dataSourceName);
	}

	public DbImpl(String dataSourceName, DataSource dataSource) {
		this.dataSourceName = dataSourceName;
		this.dataSource = dataSource;
	}

	@Override
	public Dao getDao() {
		return this.getDao(true);
	}

	@Override
	public Dao getDao(boolean autoClose) {

		ButterflyDb bdb = new ButterflyDb(this.dataSource);
		Dao dao = createDao(bdb,autoClose);

		return dao;

	}
	
	protected Dao createDao( ButterflyDb bdb , boolean autoClose ) {

		DaoImpl daoImpl = new DaoImpl( bdb , autoClose );

		daoImpl.setArgProcessor( DbUtil.DBFACTORY.getArgProcessor() );
		daoImpl.setOrmProcessor( DbUtil.DBFACTORY.getOrmProcessor() );

		Optional<PagingProcessor> pagingProcessor = DbUtil.DBFACTORY.getPagingProcessor();
		if ( pagingProcessor.isPresent() ) {
			daoImpl.setPagingProcessor( pagingProcessor.get() );
		}

		return daoImpl;
	}

}
