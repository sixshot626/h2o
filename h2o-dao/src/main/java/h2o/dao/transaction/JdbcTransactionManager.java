package h2o.dao.transaction;


import h2o.dao.Dao;
import h2o.dao.DbUtil;
import h2o.dao.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTransactionManager implements TransactionManager , Closeable {

	private static final Logger log = LoggerFactory.getLogger( JdbcTransactionManager.class.getName() );

	private String name;
	private Connection connection;
	private int isolationLevel;

	public JdbcTransactionManager(String name) {
		this.name = name;
		this.isolationLevel = -1;
	}

	public JdbcTransactionManager(String name , int isolationLevel ) {
		this.name = name;
		this.isolationLevel = isolationLevel;
	}

	@Override
	public void begin() {
		try {
			this.connection = DbUtil.getDataSource( this.name ).getConnection();
			if ( isolationLevel > -1 ) {
				connection.setTransactionIsolation( isolationLevel );
			}
			connection.setAutoCommit(false);
		} catch ( SQLException e ) {
			throw new DaoException(e);
		} finally {
			close();
		}
	}

	@Override
	public void rollBack() {
		try {
			this.connection.rollback();
		} catch ( SQLException e ) {
			throw new DaoException(e);
		} finally {
			close();
		}
	}

	@Override
	public void commit() {
		try {
			this.connection.rollback();
		} catch ( SQLException e ) {
			throw new DaoException(e);
		} finally {
			close();
		}
	}

	@Override
	public Dao getDao() {
		if ( connection == null ) {
			throw new DaoException("Connection is null");
		}
		Dao dao = DbUtil.getDb( this.name ).createDao( this.connection );
		dao.setAutoClose(false);
		return dao;
	}

	@Override
	public void close() {
		try {
			if ( connection != null ) {
				this.connection.close();
				this.connection = null;
			}
		} catch ( SQLException e ) {
			log.warn( "close" , e );
		}
	}
}
