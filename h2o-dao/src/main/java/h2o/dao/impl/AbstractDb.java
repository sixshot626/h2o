package h2o.dao.impl;

import h2o.common.exception.ExceptionUtil;
import h2o.dao.Dao;
import h2o.dao.DaoCallback;
import h2o.dao.Db;
import h2o.dao.transaction.ScopeManager;
import h2o.dao.transaction.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractDb implements Db {

    private static final Logger log = LoggerFactory.getLogger( AbstractDb.class.getName() );

    private volatile ScopeManager scopeManager;

	private volatile TransactionManager transactionManager;

	@Override
	public void setScopeManager(ScopeManager scopeManager) {
		this.scopeManager = scopeManager;
	}

	@Override
	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	protected ScopeManager getScopeManager() {
		return scopeManager;
	}

	protected TransactionManager getTransactionManager() {
		return transactionManager;
	}



	@Override
	public abstract Dao getDao();

	@Override
	public <T> T q( DaoCallback<T> daoCallback) {
		
		Dao dao = this.getDao(false);

        ScopeManager scopeManager = this.getScopeManager();
		Object scope = scopeManager == null ? null : scopeManager.beginScope(dao);

		try {
			
			return daoCallback.doCallback( dao , scope );
			
		} catch (Exception e) {

			log.debug("doCallback",e);
			throw ExceptionUtil.toRuntimeException(e);

		} finally {
			
			if ( scopeManager != null ) try {
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
		
	
		Dao dao = this.getDao( false );

        TransactionManager txManager = this.getTransactionManager();
        Object txObj = txManager == null ? null : txManager.beginTransaction(dao);

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
