package h2o.dao.impl;

import com.jenkov.db.itf.IDaos;
import com.jenkov.db.itf.IResultSetProcessor;
import com.jenkov.db.itf.PersistenceException;
import h2o.common.Mode;
import h2o.common.exception.ExceptionUtil;
import h2o.common.lang.Val;
import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.collection.ListBuilder;
import h2o.dao.Dao;
import h2o.dao.ResultSetCallback;
import h2o.dao.exception.DaoException;
import h2o.dao.jdbc.JdbcDao;
import h2o.dao.jdbc.PreparedSqlAndParameters;
import h2o.dao.jdbc.PreparedStatementManagerBatch;
import h2o.dao.jdbc.parameter.SqlParameterUtil;
import h2o.dao.log.LogWriter;
import h2o.dao.sql.SqlSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;



public class DaoImpl extends AbstractDao implements Dao {

	private static final Logger log = LoggerFactory.getLogger( Dao.class.getName() );

	private static boolean SHOWSQL = !Mode.isUserMode("DONT_SHOW_SQL");


	private final JdbcDao jdbcDao;

	public DaoImpl(Connection connection) {
		super(connection);
		this.jdbcDao = new JdbcDao(connection);
		this.jdbcDao.setAutoClose(false);
	}



	@SuppressWarnings("unchecked")
	@Override
	public <T> Val<T> getField(SqlSource sqlSource, String fieldName , Object... args) throws DaoException {

		try {

			Map<String, Object> paramMap = this.argProc(args);
			String sql = sqlSource.getSql( paramMap );

			if ( SHOWSQL ) {
				log.info("SQL:getField({})#\r\n{}\r\nPARA:{}\r\n", fieldName, sql, paramMap );
			}
			{
				LogWriter writer = this.getLogWriter();
				if (writer != null) {
					writer.write("getField(" + fieldName + ")", sql , paramMap);
				}
			}


			 if(fieldName == null) {
				 return jdbcDao.readObject(sql, paramMap);
			 } else {
                 Map<String, Object> data = jdbcDao.readMap(sql, paramMap);
                 return data == null ? Val.empty() : new Val<>( (T) data.get(fieldName) );
			 }

		} catch (Exception e) {
			throw new DaoException(e);
		}  finally {
			autoCloseDao();
		}

	}
	
	@Override
	public <T> List<T> loadFields(SqlSource sqlSource , final String fieldName , Object... args) throws DaoException {
		try {

			Map<String, Object> paramMap = this.argProc(args);
			String sql = sqlSource.getSql( paramMap );

			if ( SHOWSQL ) {
				log.info("SQL:loadFields({})#\r\n{}\r\nPARA:{}\r\n", fieldName, sql, paramMap );
			}
			{
				LogWriter writer = this.getLogWriter();
				if (writer != null) {
					writer.write("loadFields(" + fieldName + ")", sql , paramMap);
				}
			}

			return jdbcDao.read(sql, new IResultSetProcessor() {
				
				private final List<T> r = ListBuilder.newList();

				@Override
				public void init(ResultSet result , IDaos daos) throws SQLException, PersistenceException {
				}

				@SuppressWarnings("unchecked")
				@Override
				public void process(ResultSet result , IDaos daos) throws SQLException, PersistenceException {
					Object f = fieldName == null ? result.getObject(1) : result.getObject(fieldName);					
					r.add( (T)f );
				}

				@Override
				public Object getResult() throws PersistenceException {
					return r;
				}
				
			} , paramMap);

		} catch (Exception e) {
			throw new DaoException(e);
		} finally {
			autoCloseDao();
		}
	}

	@Override
	public Val<Map<String, Object>> get(SqlSource sqlSource, Object... args) throws DaoException {

		try {

			Map<String, Object> paramMap = this.argProc(args);
			String sql = sqlSource.getSql( paramMap );

			if ( SHOWSQL ) {
				log.info("SQL:get#\r\n{}\r\nPARA:{}\r\n", sql, paramMap );
			}
			{
				LogWriter writer = this.getLogWriter();
				if (writer != null) {
					writer.write("get", sql , paramMap);
				}
			}

			Map<String, Object> r = jdbcDao.readMap(sql, paramMap);
			return r == null ? Val.empty() : new Val<>(r);

		} catch (Exception e) {
			throw new DaoException(e);
		} finally {
			autoCloseDao();
		}
	}

	@Override
	public List<Map<String, Object>> load(SqlSource sqlSource, Object... args) throws DaoException {
		try {
			Map<String, Object> paramMap = this.argProc(args);
			String sql = sqlSource.getSql( paramMap );

			if ( SHOWSQL ) {
				log.info("SQL:load#\r\n{}\r\nPARA:{}\r\n", sql, paramMap );
			}
			{
				LogWriter writer = this.getLogWriter();
				if (writer != null) {
					writer.write("load", sql , paramMap);
				}
			}

			return jdbcDao.readMapList(sql, paramMap);

		} catch (Exception e) {
			throw new DaoException(e);
		} finally {
			autoCloseDao();
		}
	}





	@SuppressWarnings("unchecked")
	@Override
	public <T> Val<T> load(final ResultSetCallback<T> rsCallback, SqlSource sqlSource, Object... args) throws DaoException {
		
		try {
			Map<String, Object> paramMap = this.argProc(args);
			String sql = sqlSource.getSql( paramMap );

			if ( SHOWSQL ) {
				log.info("SQL:load(ResultSetCallback)#\r\n{}\r\nPARA:{}\r\n", sql, paramMap );
			}
			{
				LogWriter writer = this.getLogWriter();
				if (writer != null) {
					writer.write("load(ResultSetCallback)", sql , paramMap);
				}
			}

			T r = (T) jdbcDao.read(sql, new IResultSetProcessor() {

				@Override
				public void init(ResultSet rs, IDaos idao) throws SQLException, PersistenceException {

					try {

						rsCallback.init(rs, DaoImpl.this);

					} catch (SQLException e) {
						throw e;
					} catch (Exception e) {
						throw ExceptionUtil.toRuntimeException(e);
					}

				}

				@Override
				public void process(ResultSet rs, IDaos idao) throws SQLException, PersistenceException {

					try {

						rsCallback.process(rs, DaoImpl.this);

					} catch (SQLException e) {
						throw e;
					} catch (Exception e) {
						throw ExceptionUtil.toRuntimeException(e);
					}

				}

				@Override
				public Object getResult() throws PersistenceException {

					try {

						return rsCallback.getResult();

					} catch (Exception e) {
						throw ExceptionUtil.toRuntimeException(e);
					}

				}

			}, paramMap);

			return r == null ? Val.empty() : new Val<>(r);

		} catch (Exception e) {
			throw new DaoException(e);
		} finally {
			autoCloseDao();
		}
	}






	@Override
	public int update(SqlSource sqlSource, Object... args) throws DaoException {
		try {
			Map<String, Object> paramMap = this.argProc(args);
			String sql = sqlSource.getSql( paramMap );

			if ( SHOWSQL ) {
				log.info("SQL:update#\r\n{}\r\nPARA:{}\r\n", sql, paramMap );
			}
			{
				LogWriter writer = this.getLogWriter();
				if (writer != null) {
					writer.write("update", sql , paramMap);
				}
			}

			return jdbcDao.update(sql, paramMap);

		} catch (Exception e) {
			throw new DaoException(e);
		} finally {
			autoCloseDao();
		}
	}


	@Override
	public int[] batchUpdate(SqlSource sqlSource, Collection<?> args) throws DaoException {
		try {
			
			String sql = sqlSource.getSql();

			List<Object[]> nArgs = ListBuilder.newList();
			String nSql = null;
			
			if( CollectionUtil.isEmpty( args ) ) {
				
				throw new IllegalArgumentException( "Args is empty." );
				
			} for( Object arg : args ) {
				
				Object[] aa;
				if( arg.getClass().isArray() ) {
					aa = (Object[])arg;
				} else {
					aa = new Object[] { arg };
				}
				
				Map<String, Object> paramMap = this.argProc(aa);

				PreparedSqlAndParameters sqlAndPara = SqlParameterUtil.toPreparedSqlAndPara(sql, paramMap);
				nArgs.add( sqlAndPara.paras );
				nSql = sqlAndPara.sql;
				
			}

			if ( SHOWSQL ) {
				log.info("SQL:batchUpdate#\r\n{}\r\n", nSql );
			}
			{
				LogWriter writer = this.getLogWriter();
				if (writer != null) {
					writer.write("batchUpdate", sql , null);
				}
			}

			PreparedStatementManagerBatch preparedStatementManagerBatch = new PreparedStatementManagerBatch(nArgs);

			jdbcDao.update(nSql, preparedStatementManagerBatch);

			return preparedStatementManagerBatch.getUpdateRows();

		} catch (Exception e) {
			throw new DaoException(e);
		} finally {
			autoCloseDao();
		}
	}





	@Override
	public void close() throws DaoException {
		try {
			this.jdbcDao.close();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}


	private void autoCloseDao() {
		if( this.isAutoClose() ) {
			this.close();
		}
	}



}
