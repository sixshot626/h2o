package h2o.dao;

import h2o.dao.exception.DaoException;
import h2o.dao.orm.ArgProcessor;
import h2o.dao.orm.OrmProcessor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Dao extends TxManager , ScopeManager {
	
	
	
	void setArgProcessor(ArgProcessor argProcessor);

	void setOrmProcessor(OrmProcessor ormProcessor);


	void setTxManager(TxManager txManager);
	TxManager getTxManager();
	
	void setScopeManager(ScopeManager scopeManager);
	ScopeManager getScopeManager();
	
	
	
	
	<T> T getField(String sql, String fieldName, Object... args) throws DaoException;

	<T> T getField(SqlSource sqlSource, String fieldName, Object... args) throws DaoException;

	
	
	
	<T> List<T> loadFields(String sql, String fieldName, Object... args) throws DaoException;
	
	<T> List<T> loadFields(SqlSource sqlSource, String fieldName, Object... args) throws DaoException;
	

	
	
	Map<String,Object> get(String sql, Object... args)  throws DaoException;
	
	Map<String,Object> get(SqlSource sqlSource, Object... args)  throws DaoException;
	
	
	
	
	List<Map<String,Object>> load(String sql, Object... args)  throws DaoException;
	
	List<Map<String,Object>> load(SqlSource sqlSource, Object... args)  throws DaoException;
	
	
	
	
	<T> T get(Class<T> clazz, String sql, Object... args)  throws DaoException;
	
	<T> T get(Class<T> clazz, SqlSource sqlSource, Object... args)  throws DaoException;
	
	
	
	
	<T> List<T> load(Class<T> clazz, String sql, Object... args)  throws DaoException;
	
	<T> List<T> load(Class<T> clazz, SqlSource sqlSource, Object... args)  throws DaoException;
	
	
	
	
	<T> T load(RsCallback<T> rsCallback, String sql, Object... args)   throws DaoException;
	
	<T> T load(RsCallback<T> rsCallback, SqlSource sqlSource, Object... args)   throws DaoException;
	
	
	
	
	int update(String sql, Object... args)  throws DaoException;
	
	int update(SqlSource sqlSource, Object... args)  throws DaoException;
	
	
	
	
	int[] batchUpdate(String sql, Collection<?> args)  throws DaoException;
	
	int[] batchUpdate(SqlSource sqlSource, Collection<?> args)  throws DaoException;


    Connection getConnection() throws SQLException;
	
	void close() throws DaoException;
	

}
