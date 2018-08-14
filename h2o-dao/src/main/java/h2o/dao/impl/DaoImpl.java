package h2o.dao.impl;

import com.jenkov.db.itf.IDaos;
import com.jenkov.db.itf.IResultSetProcessor;
import com.jenkov.db.itf.PersistenceException;
import h2o.common.collections.CollectionUtil;
import h2o.common.collections.builder.ListBuilder;
import h2o.common.collections.tuple.Tuple2;
import h2o.common.dao.butterflydb.ButterflyDao;
import h2o.common.dao.butterflydb.ButterflyDb;
import h2o.common.dao.butterflydb.impl.PreparedStatementManagerBatch;
import h2o.common.exception.ExceptionUtil;
import h2o.dao.Dao;
import h2o.dao.RsCallback;
import h2o.dao.SqlSource;
import h2o.dao.exception.DaoException;
import h2o.dao.impl.sql.TSql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static h2o.common.dao.util.SqlParameterUtil.toPreparedSqlAndPara;


public class DaoImpl extends AbstractDao implements Dao {

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

	protected Map<String, Object> argProc(Object... args) {
		return this.argProcessor.proc(args);
	}

	protected <T> T ormProc(Map<String, Object> row, Class<T> clazz) {
		return this.ormProcessor.proc(row, clazz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getField(SqlSource sqlSource, String fieldName , Object... args) throws DaoException {

		try {

			Map<String, Object> paramMap = this.argProc(args);
			String sql = sqlSource.getSql( paramMap );

			 if(fieldName == null) {
				 return getBDao().readObject(sql, paramMap);
			 } else {
                 Map<String, Object> data = getBDao().readMap(sql, paramMap);
                 return data == null ? null : (T) data.get(fieldName);
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
	public Map<String, Object> get(SqlSource sqlSource, Object... args) throws DaoException {

		try {

			Map<String, Object> paramMap = this.argProc(args);
			String sql = sqlSource.getSql( paramMap );

			return getBDao().readMap(sql, paramMap);

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<Map<String, Object>> load(SqlSource sqlSource, Object... args) throws DaoException {
		try {
			Map<String, Object> paramMap = this.argProc(args);
			String sql = sqlSource.getSql( paramMap );

			return getBDao().readMapList(sql, paramMap);

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public <T> T get(Class<T> clazz, SqlSource sqlSource, Object... args) throws DaoException {
		try {
			Map<String, Object> row = this.get(sqlSource, args);

			return this.ormProc(row, clazz);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public <T> List<T> load(Class<T> clazz, SqlSource sqlSource, Object... args) throws DaoException {
		try {
			List<Map<String, Object>> rows = this.load(sqlSource, args);

			List<T> objs = new ArrayList<T>(rows.size());

			for (Map<String, Object> row : rows) {
				objs.add(this.ormProc(row, clazz));
			}

			return objs;

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public int update(SqlSource sqlSource, Object... args) throws DaoException {
		try {
			Map<String, Object> paramMap = this.argProc(args);
			String sql = sqlSource.getSql( paramMap );

			return getBDao().update(sql, paramMap);

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T load(final RsCallback<T> rsCallback, SqlSource sqlSource, Object... args) throws DaoException {
		
		try {
			Map<String, Object> paramMap = this.argProc(args);
			String sql = sqlSource.getSql( paramMap );

			return (T) getBDao().read(sql, new IResultSetProcessor() {

				@Override
				public void init(ResultSet rs, IDaos idao) throws SQLException, PersistenceException {

					try {

						rsCallback.init( rs, DaoImpl.this );

					} catch (SQLException e) {
						throw e;
					} catch (Exception e) {
						throw ExceptionUtil.toRuntimeException(e);
					}

				}

				@Override
				public void process(ResultSet rs, IDaos idao) throws SQLException, PersistenceException {

					try {

						rsCallback.process( rs, DaoImpl.this);

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

			PreparedStatementManagerBatch preparedStatementManagerBatch = new PreparedStatementManagerBatch(nArgs);

			getBDao().update(nSql, preparedStatementManagerBatch);

			return preparedStatementManagerBatch.getUpdateRows();

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	
	
	
	
	
	
	

	@Override
	public <T> T getField(String sql, String fieldName, Object... args) throws DaoException {
		return this.getField( new TSql(sql) , fieldName, args );
	}

	@Override
	public <T> List<T> loadFields(String sql, String fieldName, Object... args) throws DaoException {
		return this.loadFields( new TSql(sql) , fieldName, args );
	}

	@Override
	public Map<String, Object> get(String sql, Object... args) throws DaoException {
		return this.get( new TSql(sql) , args );
	}

	@Override
	public List<Map<String, Object>> load(String sql, Object... args) throws DaoException {
		return this.load( new TSql(sql), args );
	}

	@Override
	public <T> T get(Class<T> clazz, String sql, Object... args) throws DaoException {
		return this.get( clazz,  new TSql(sql), args );
	}

	@Override
	public <T> List<T> load(Class<T> clazz, String sql, Object... args) throws DaoException {
		return this.load( clazz, new TSql(sql), args );
	}

	@Override
	public <T> T load(RsCallback<T> rsCallback, String sql, Object... args) throws DaoException {
		return this.load( rsCallback, new TSql(sql), args );
	}

	@Override
	public int update(String sql, Object... args) throws DaoException {
		return this.update( new TSql(sql), args );
	}

	@Override
	public int[] batchUpdate(String sql, Collection<?> args) throws DaoException {
		return this.batchUpdate( new TSql(sql), args );
	}


    @Override
    public Connection getConnection() throws SQLException {
        return bdb.persistenceManager.getDataSource().getConnection();
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
