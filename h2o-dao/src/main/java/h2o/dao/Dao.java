package h2o.dao;

import h2o.common.data.domain.Page;
import h2o.common.data.domain.PageRequest;
import h2o.common.lang.Val;
import h2o.dao.exception.DaoException;
import h2o.dao.orm.ArgProcessor;
import h2o.dao.orm.OrmProcessor;
import h2o.dao.page.PagingProcessor;
import h2o.dao.sql.SqlSource;

import javax.sql.DataSource;
import java.io.Closeable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Dao extends Closeable {



	void setArgProcessor(ArgProcessor argProcessor);

	void setOrmProcessor(OrmProcessor ormProcessor);

	void setPagingProcessor(PagingProcessor pagingProcessor);



	<T> Val<T> getField(String sql, String fieldName, Object... args) throws DaoException;

	<T> Val<T> getField(SqlSource sqlSource, String fieldName, Object... args) throws DaoException;



	<T> List<T> loadFields(String sql, String fieldName, Object... args) throws DaoException;

	<T> List<T> loadFields(SqlSource sqlSource, String fieldName, Object... args) throws DaoException;



	Val<Map<String,Object>> get(String sql, Object... args)  throws DaoException;

	Val<Map<String,Object>> get(SqlSource sqlSource, Object... args)  throws DaoException;



	List<Map<String,Object>> load(String sql, Object... args)  throws DaoException;
	
	List<Map<String,Object>> load(SqlSource sqlSource, Object... args)  throws DaoException;



	<T> Val<T> get(Class<T> clazz, String sql, Object... args)  throws DaoException;

	<T> Val<T> get(Class<T> clazz, SqlSource sqlSource, Object... args)  throws DaoException;



	<T> List<T> load(Class<T> clazz, String sql, Object... args)  throws DaoException;
	
	<T> List<T> load(Class<T> clazz, SqlSource sqlSource, Object... args)  throws DaoException;



	<T> Val<T> load(ResultSetCallback<T> rsCallback, String sql, Object... args)   throws DaoException;

	<T> Val<T> load(ResultSetCallback<T> rsCallback, SqlSource sqlSource, Object... args)   throws DaoException;



	Page<Map<String,Object>> pagingLoad(String sql, PageRequest pageRequest,  Object... args) throws DaoException;;

    Page<Map<String,Object>> pagingLoad(SqlSource sqlSource, PageRequest pageRequest,  Object... args) throws DaoException;;



    <T> Page<T> pagingLoad(Class<T> clazz, String sql, PageRequest pageRequest, Object... args)  throws DaoException;

    <T> Page<T> pagingLoad(Class<T> clazz, SqlSource sqlSource, PageRequest pageRequest, Object... args)  throws DaoException;



	int update(String sql, Object... args)  throws DaoException;
	
	int update(SqlSource sqlSource, Object... args)  throws DaoException;



	int[] batchUpdate(String sql, Collection<?> args)  throws DaoException;
	
	int[] batchUpdate(SqlSource sqlSource, Collection<?> args)  throws DaoException;



	DataSource getDataSource();


	void close() throws DaoException;
	

}
