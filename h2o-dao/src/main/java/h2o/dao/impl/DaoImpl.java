package h2o.dao.impl;

import com.jenkov.db.itf.IDaos;
import com.jenkov.db.itf.IResultSetProcessor;
import com.jenkov.db.itf.PersistenceException;
import h2o.common.Mode;
import h2o.common.dao.butterflydb.ButterflyDao;
import h2o.common.dao.butterflydb.ButterflyDb;
import h2o.common.dao.butterflydb.impl.PreparedStatementManagerBatch;
import h2o.common.exception.ExceptionUtil;
import h2o.common.lang.Val;
import h2o.common.lang.tuple.Tuple2;
import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.collection.ListBuilder;
import h2o.dao.Dao;
import h2o.dao.ResultSetCallback;
import h2o.dao.exception.DaoException;
import h2o.dao.sql.SqlSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static h2o.common.dao.util.SqlParameterUtil.toPreparedSqlAndPara;


public class DaoImpl extends AbstractDao implements Dao {

	private static final Logger log = LoggerFactory.getLogger( Dao.class.getName() );

	private static boolean SHOWSQL = !Mode.isUserMode("DONT_SHOW_SQL");

	private final boolean autoClose;

	private final ButterflyDb bdb;
	private ButterflyDao bdao;


	public DaoImpl(ButterflyDb bdb) {
		this(bdb, true);
	}

	public DaoImpl(ButterflyDb bdb, boolean autoClose) {
		this.autoClose = autoClose;
		this.bdb = bdb;
	}

	private ButterflyDao getBDao() {
		if (autoClose) {
			return bdb.getDao(true);
		} else {
			if (bdao == null) {
				bdao = bdb.getDao(false);
			}
			return bdao;
		}
	}



	@SuppressWarnings("unchecked")
	@Override
	public <T> Val<T> getField(SqlSource sqlSource, String fieldName , Object... args) throws DaoException {

		try {

			Map<String, Object> paramMap = this.argProc(args);
			String sql = sqlSource.getSql( paramMap );

			if ( SHOWSQL ) {
				log.info("getField({})#\r\n          SQL:{{}};\r\n          PARA:{}\r\n", fieldName, sql, paramMap );
			}

			 if(fieldName == null) {
				 return getBDao().readObject(sql, paramMap);
			 } else {
                 Map<String, Object> data = getBDao().readMap(sql, paramMap);
                 return data == null ? Val.empty() : new Val<>( (T) data.get(fieldName) );
			 }

		} catch (Exception e) {
			throw new DaoException(e);
		}

	}
	
	@Override
	public <T> List<T> loadFields(SqlSource sqlSource , final String fieldName , Object... args) throws DaoException {
		try {

			Map<String, Object> paramMap = this.argProc(args);
			String sql = sqlSource.getSql( paramMap );

			if ( SHOWSQL ) {
				log.info("loadFields({})#\r\n          SQL:{{}};\r\n          PARA:{}\r\n", fieldName, sql, paramMap );
			}

			return getBDao().read(sql, new IResultSetProcessor() {
				
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
		}
	}

	@Override
	public Val<Map<String, Object>> get(SqlSource sqlSource, Object... args) throws DaoException {

		try {

			Map<String, Object> paramMap = this.argProc(args);
			String sql = sqlSource.getSql( paramMap );

			if ( SHOWSQL ) {
				log.info("get#\r\n          SQL:{{}};\r\n          PARA:{}\r\n", sql, paramMap );
			}

			Map<String, Object> r = getBDao().readMap(sql, paramMap);
			return r == null ? Val.empty() : new Val<>(r);

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<Map<String, Object>> load(SqlSource sqlSource, Object... args) throws DaoException {
		try {
			Map<String, Object> paramMap = this.argProc(args);
			String sql = sqlSource.getSql( paramMap );

			if ( SHOWSQL ) {
				log.info("load#\r\n          SQL:{{}};\r\n          PARA:{}\r\n", sql, paramMap );
			}

			return getBDao().readMapList(sql, paramMap);

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}





	@SuppressWarnings("unchecked")
	@Override
	public <T> Val<T> load(final ResultSetCallback<T> rsCallback, SqlSource sqlSource, Object... args) throws DaoException {
		
		try {
			Map<String, Object> paramMap = this.argProc(args);
			String sql = sqlSource.getSql( paramMap );

			if ( SHOWSQL ) {
				log.info("load(ResultSetCallback)#\r\n          SQL:{{}};\r\n          PARA:{}\r\n", sql, paramMap );
			}

			T r = (T) getBDao().read(sql, new IResultSetProcessor() {

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
		}
	}






	@Override
	public int update(SqlSource sqlSource, Object... args) throws DaoException {
		try {
			Map<String, Object> paramMap = this.argProc(args);
			String sql = sqlSource.getSql( paramMap );

			if ( SHOWSQL ) {
				log.info("update#\r\n          SQL:{{}};\r\n          PARA:{}\r\n", sql, paramMap );
			}

			return getBDao().update(sql, paramMap);

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}


	@Override
	public int[] batchUpdate(SqlSource sqlSource, Collection<?> args) throws DaoException {
		try {
			
			String sql = sqlSource.getSql();

			List<Object[]> nArgs = ListBuilder.newList();
			String nSql = null;
			
			if( CollectionUtil.isBlank( args ) ) {
				
				throw new IllegalArgumentException( "Args is empty." );
				
			} for( Object arg : args ) {
				
				Object[] aa;
				if( arg.getClass().isArray() ) {
					aa = (Object[])arg;
				} else {
					aa = new Object[] { arg };
				}
				
				Map<String, Object> paramMap = this.argProc(aa);
				
				Tuple2<String,Object[]> sqlAndPara = toPreparedSqlAndPara(sql, paramMap);
				nArgs.add( sqlAndPara.e1 );
				nSql = sqlAndPara.e0;
				
			}

			if ( SHOWSQL ) {
				log.info("batchUpdate#\r\n          SQL:{{}};\r\n", nSql );
			}

			PreparedStatementManagerBatch preparedStatementManagerBatch = new PreparedStatementManagerBatch(nArgs);

			getBDao().update(nSql, preparedStatementManagerBatch);

			return preparedStatementManagerBatch.getUpdateRows();

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}




	@Override
	public DataSource getDataSource() {
		return bdb.persistenceManager.getDataSource();
	}



    @Override
	public void close() throws DaoException {
		if (this.bdao != null) {
			try {
				
				this.bdao.closeConnection();
				this.bdao = null;
				
			} catch (Exception e) {
				throw new DaoException(e);
			}
		}
	}



}
