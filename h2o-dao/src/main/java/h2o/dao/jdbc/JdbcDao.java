package h2o.dao.jdbc;


import com.jenkov.db.impl.Daos;
import com.jenkov.db.itf.*;
import h2o.common.Mode;
import h2o.common.collection.IgnoreCaseMap;
import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.collection.ListBuilder;
import h2o.dao.exception.DaoException;
import h2o.dao.jdbc.parameter.SqlParameterUtil;
import h2o.dao.jdbc.rowproc.RowDataProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;



@SuppressWarnings({"rawtypes","unchecked"})
public class JdbcDao implements Closeable {

    private static final Logger log = LoggerFactory.getLogger( JdbcDao.class.getName() );

    private static boolean SHOWSQL = Mode.isUserMode("SHOW_BUTTERFLY_SQL");

    private final IDaos daos;
	private final IJdbcDao jdbcDao;
	private final IMapDao mapDao;

	private boolean autoClose = true;



	public JdbcDao(Connection connection) {
		this.daos = new Daos(connection);
		this.jdbcDao = daos.getJdbcDao();
		this.mapDao = daos.getMapDao();
	}
	


	public void setAutoClose(boolean autoClose) {
		this.autoClose = autoClose;
	}

	public Long readLong(String sql) {

	    if ( SHOWSQL ) {
            log.info("readLong(sql):{}", sql);
        }

		try {
			return jdbcDao.readLong(sql);
		} catch (PersistenceException e) {
			throw new DaoException(e);
		} finally {
			autoCloseConnection();
		}

	}

	public Long readLong(String sql, Object... parameters) {

	    if ( SHOWSQL ) {
            log.info("readLong(sql, Object...):{}, para:{}", sql, Arrays.asList(parameters));
        }

		try {
			return jdbcDao.readLong(sql, parameters);
		} catch (PersistenceException e) {
			throw new DaoException(e);
		} finally {
			autoCloseConnection();
		}

	}
	

	public Long readLong(String sql, Map paramMap) {

	    if ( SHOWSQL ) {
            log.info("readLong(sql, Map):{}, para:{}", sql, paramMap);
        }

		PreparedSqlAndParameters sqlAndPara = toPreparedSqlAndPara(sql, paramMap);
		
		return readLong(sqlAndPara.sql,sqlAndPara.paras);

	}


	public String readIdString(String sql) {

	    if ( SHOWSQL ) {
            log.info("readIdString(sql]):{}", sql);
        }

		try {
			return jdbcDao.readIdString(sql);
		} catch (PersistenceException e) {
			throw new DaoException(e);
		} finally {
			autoCloseConnection();
		}

	}

	public String readIdString(String sql, Object... parameters) {

	    if ( SHOWSQL ) {
            log.info("readIdString(sql, Object...):{}, para:{}", sql, Arrays.asList(parameters));
        }

		try {
			return jdbcDao.readIdString(sql, parameters);
		} catch (PersistenceException e) {
			throw new DaoException(e);
		} finally {
			autoCloseConnection();
		}

	}
	

	public String readIdString(String sql,  Map paramMap) {

	    if ( SHOWSQL ) {
            log.info("readIdString(sql, Map):{}, para:{}", sql, paramMap);
        }

		PreparedSqlAndParameters sqlAndPara = toPreparedSqlAndPara(sql, paramMap);
		
		return readIdString(sqlAndPara.sql,sqlAndPara.paras);

	}
	

	public String readIdString(String sql, IPreparedStatementManager statementManager) {

	    if ( SHOWSQL ) {
            log.info("readIdString(sql,IPreparedStatementManager):{}", sql);
        }

		try {
			return jdbcDao.readIdString(sql, statementManager);
		} catch (PersistenceException e) {
			throw new DaoException(e);
		} finally {
			autoCloseConnection();
		}

	}

	public <T> T read(String sql, IResultSetProcessor processor) {

	    if ( SHOWSQL ) {
            log.info("read(sql,IResultSetProcessor):{}", sql);
        }

		try {
			return (T)jdbcDao.read(sql, processor);
		} catch (PersistenceException e) {
			throw new DaoException(e);
		} finally {
			autoCloseConnection();
		}

	}

	public <T> T read(String sql, IResultSetProcessor processor, Object... parameters) {

	    if ( SHOWSQL ) {
            log.info("read(sql,IResultSetProcessor,Object...):{}, para:{}", sql, Arrays.asList(parameters));
        }

		try {
			return (T)jdbcDao.read(sql, processor, parameters);
		} catch (PersistenceException e) {
			throw new DaoException(e);
		} finally {
			autoCloseConnection();
		}

	}
	

	public <T> T read(String sql, IResultSetProcessor processor,  Map paramMap) {

	    if ( SHOWSQL ) {
            log.info("read(sql,IResultSetProcessor,Map):{}, para:{}", sql, paramMap);
        }

		PreparedSqlAndParameters sqlAndPara = toPreparedSqlAndPara(sql, paramMap);
		
		return read(sqlAndPara.sql,processor,sqlAndPara.paras);

	}

	public <T> T read(String sql, IPreparedStatementManager statementManager, IResultSetProcessor processor) {

	    if ( SHOWSQL ) {
            log.info("read(sql,IPreparedStatementManager,IResultSetProcessor):{}", sql);
        }

		try {
			return (T)jdbcDao.read(sql, statementManager, processor);
		} catch (PersistenceException e) {
			throw new DaoException(e);
		} finally {
			autoCloseConnection();
		}

	}

	public int update(String sql) {

	    if ( SHOWSQL ) {
            log.info("update(sql):{}", sql);
        }

		try {
			return jdbcDao.update(sql);
		} catch (PersistenceException e) {
			throw new DaoException(e);
		} finally {
			autoCloseConnection();
		}

	}

	public int update(String sql, Object... parameters) {

	    if ( SHOWSQL ) {
            log.info("update(sql, Object...):{}, para:{}", sql, Arrays.asList(parameters));
        }

		try {
			return jdbcDao.update(sql, parameters);
		} catch (PersistenceException e) {
			throw new DaoException(e);
		} finally {
			autoCloseConnection();
		}

	}
	

	public int update(String sql, Map paramMap) {

	    if ( SHOWSQL ) {
            log.info("update(sql, Map):{}, para:{}", sql, paramMap);
        }

		PreparedSqlAndParameters sqlAndPara = toPreparedSqlAndPara(sql, paramMap);
		
		return update(sqlAndPara.sql,sqlAndPara.paras);
	}

	public int update(String sql, IPreparedStatementManager statementManager) {

	    if ( SHOWSQL ) {
            log.info("update(sql, IPreparedStatementManager):{}", sql);
        }

		try {
			return jdbcDao.update(sql, statementManager);
		} catch (PersistenceException e) {
			throw new DaoException(e);
		} finally {
			autoCloseConnection();
		}

	}
	
	
	
	
	
	
	
	

	
	
	//=====================================================//
	// mapDao
	
	
	private volatile RowDataProcessor rowDataProcessor;
	
	public JdbcDao setRowDataProcessor(RowDataProcessor rowDataProcessor) {
		this.rowDataProcessor = rowDataProcessor;
		return this;
	}

	private Map<String,Object> dataProc( Map<String,Object> m) {
		
		RowDataProcessor rdp = rowDataProcessor;
		if( rdp != null ) {
			return rdp.dataProc(m);
		} else {
			return m == null ? null : new IgnoreCaseMap<Object>( m );
		}
		
	}
	
	private List<Map<String,Object>> listProc( List<Map<String,Object>> ml) {
		
		List<Map<String,Object>> nl = ListBuilder.newList();
		for( Map<String,Object> m : ml ) {
			nl.add(dataProc(m));
		}
		
		return nl;
	}
	
	
	
	
	
	
	
	public <T> T readObject(String sql) {

	    if ( SHOWSQL ) {
            log.info("readObject(sql):{}", sql);
        }
		
		try {
			
			Map data = mapDao.readMap(sql);
			return CollectionUtil.isEmpty( data ) ? null : (T)data.values().iterator().next();
			
		} catch (PersistenceException e) {
			throw new DaoException(e);
		} finally {
			autoCloseConnection();
		}
		
	}

	public <T> T readObject(String sql, Object... parameters) {

	    if ( SHOWSQL ) {
            log.info("readObject(sql, Object...):{}, para:{}", sql, Arrays.asList(parameters));
        }

		try {
			
			Map data = mapDao.readMap(sql, parameters);
			return CollectionUtil.isEmpty( data ) ? null : (T)data.values().iterator().next();

		} catch (PersistenceException e) {
			throw new DaoException(e);
		} finally {
			autoCloseConnection();
		}
	}
	

	public <T> T readObject(String sql, Map paramMap) {

        if ( SHOWSQL ) {
            log.info("readObject(sql, Map):{}, para:{}", sql, paramMap);
        }

		PreparedSqlAndParameters sqlAndPara = toPreparedSqlAndPara(sql, paramMap);
		
		return readObject(sqlAndPara.sql,sqlAndPara.paras);
	}

	
	
	
	
	
	
	
	
	
	
	public Map<String,Object> readMap(String sql) {

	    if ( SHOWSQL ) {
            log.info("readMap(sql):{}", sql);
        }

		try {
			return dataProc( mapDao.readMap(sql) );
		} catch (PersistenceException e) {
			throw new DaoException(e);
		} finally {
			autoCloseConnection();
		}

	}


	public Map<String,Object> readMap(String sql, Object... parameters) {

	    if ( SHOWSQL ) {
            log.info("readMap(sql, Object...):{}, para:{}", sql, Arrays.asList(parameters));
        }

		try {
			return dataProc( mapDao.readMap(sql, parameters) );
		} catch (PersistenceException e) {
			throw new DaoException(e);
		} finally {
			autoCloseConnection();
		}

	}	

	public Map<String,Object> readMap(String sql, Map paramMap) {

	    if ( SHOWSQL ) {
            log.info("readMap(sql, Map):{}, para:{}", sql, paramMap);
        }

		PreparedSqlAndParameters sqlAndPara = toPreparedSqlAndPara(sql, paramMap);
		
		return readMap(sqlAndPara.sql,sqlAndPara.paras);

	}


	public Map<String,Object> readMap(String sql, IPreparedStatementManager statementManager) {

	    if ( SHOWSQL ) {
            log.info("readMap(sql, IPreparedStatementManager):{}", sql);
        }

		try {
			return dataProc( mapDao.readMap(sql, statementManager) );
		} catch (PersistenceException e) {
			throw new DaoException(e);
		} finally {
			autoCloseConnection();
		}

	}



	
	
	public List<Map<String,Object>> readMapList(String sql) {

	    if ( SHOWSQL ) {
            log.info("readMapList(sql):{}", sql);
        }

		try {
			return listProc( mapDao.readMapList(sql) );
		} catch (PersistenceException e) {
			throw new DaoException(e);
		} finally {
			autoCloseConnection();
		}

	}
	

	public List<Map<String,Object>> readMapList(String sql, Object... parameters) {

	    if ( SHOWSQL ) {
            log.info("readMapList(sql, Object...):{}, para:{}", sql, Arrays.asList(parameters));
        }

		try {
			return listProc( mapDao.readMapList(sql, parameters) );
		} catch (PersistenceException e) {
			throw new DaoException(e);
		} finally {
			autoCloseConnection();
		}

	}
	
	public List<Map<String,Object>> readMapList(String sql, Map paramMap) {

	    if ( SHOWSQL ) {
            log.info("readMapList(sql, Map):{}, para:{}", sql, paramMap);
        }

		PreparedSqlAndParameters sqlAndPara = toPreparedSqlAndPara(sql, paramMap);
		
		return readMapList(sqlAndPara.sql,sqlAndPara.paras);

	}


	public List<Map<String,Object>> readMapList(String sql, IPreparedStatementManager statementManager) {

	    if ( SHOWSQL ) {
            log.info("readMapList(sql, IPreparedStatementManager):{}", sql);
        }

		try {
			return listProc( mapDao.readMapList(sql, statementManager) );
		} catch (PersistenceException e) {
			throw new DaoException(e);
		} finally {
			autoCloseConnection();
		}

	}









	private PreparedSqlAndParameters toPreparedSqlAndPara(String sql, Map paramMap ) {
		return SqlParameterUtil.toPreparedSqlAndPara( sql, paramMap);
	}




	@Override
	public void close() throws DaoException {
		try {
			this.daos.closeConnection();
		} catch (PersistenceException e) {
			log.warn("closeConnection", e);
		}
	}

	private void autoCloseConnection() {
		if( this.autoClose ) {
			this.close();
		}
	}

	

}
