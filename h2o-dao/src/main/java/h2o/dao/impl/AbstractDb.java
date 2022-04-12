package h2o.dao.impl;

import h2o.common.exception.ExceptionUtil;
import h2o.dao.Dao;
import h2o.dao.DaoCallback;
import h2o.dao.Db;
import h2o.dao.DbUtil;
import h2o.dao.transaction.ScopeManager;
import h2o.dao.transaction.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Optional;


public abstract class AbstractDb implements Db {

    private static final Logger log = LoggerFactory.getLogger( AbstractDb.class.getName() );


	private final ScopeManager scopeManager;

	public AbstractDb(ScopeManager scopeManager) {
		this.scopeManager = scopeManager;
	}

	@Override
	public ScopeManager getScopeManager() {
		return scopeManager;
	}


	@Override
	public <T> T q( DaoCallback<T> daoCallback) {

		ScopeManager scopeManager = this.getScopeManager();
		Object scope = scopeManager.beginScope();

		Dao dao = this.getDao();

		try {

			return daoCallback.doCallback( dao , scope );

		} catch (Exception e) {

			log.debug("doCallback",e);
			throw ExceptionUtil.toRuntimeException(e);

		} finally {

			try {
				scopeManager.endScope(scope);
			} catch ( Exception e ) {
				log.error("endScope",e);
			}

			try {
				dao.close();
			} catch( Exception e ) {
				log.error("dao.close",e);
			}

		}

	}

	@Override
	public <T> T tx( DaoCallback<T> daoCallback) {


		ScopeManager scopeManager = this.getScopeManager();
		TransactionManager txManager = scopeManager instanceof TransactionManager ?
				(TransactionManager) this.getScopeManager() : null;

		Object txObj = null;

		if ( txManager != null ) {
			txObj = txManager.beginTransaction();
		}

		Dao dao = this.getDao();

		try {
			
			T t = daoCallback.doCallback(dao , txObj);

			if ( txManager != null ) {
				txManager.commit(txObj);
			}
			
			return t;
			
		} catch (Exception e) {

			log.debug("doCallback",e);

			if ( txManager != null ) {
				txManager.rollBack(txObj, e);
			}

			throw ExceptionUtil.toRuntimeException(e);

		} finally {
			
			try {
				dao.close();
			} catch( Exception e ) {
				log.error("dao.close",e);
			}

		}
	}


	

}
