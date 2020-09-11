package h2o.common.dao.butterflydb;


import com.jenkov.db.itf.*;
import h2o.common.Mode;
import h2o.common.collections.CollectionUtil;
import h2o.common.collections.IgnoreCaseMap;
import h2o.common.collections.builder.ListBuilder;
import h2o.common.collections.tuple.Tuple2;
import h2o.common.dao.rowproc.RowDataProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static h2o.common.dao.util.SqlParameterUtil.toPreparedSqlAndPara;


@SuppressWarnings({"rawtypes","unchecked"})
public class ButterflyDao {

    private static final Logger log = LoggerFactory.getLogger( ButterflyDao.class.getName() );

    private static boolean SHOWSQL = Mode.isUserMode("DONT_SHOW_SQL") ? false : true;

    private final IDaos daos;
	private final IJdbcDao jdbcDao;
	private final IMapDao mapDao;
	
	private boolean autoClose;
	
	public ButterflyDao( IDaos daos) {
		this.daos = daos;
		this.jdbcDao = daos.getJdbcDao();
		this.mapDao = daos.getMapDao();
	}
	
	public ButterflyDao( IDaos daos , boolean autoClose ) {
		this.daos = daos;
		this.jdbcDao = daos.getJdbcDao();
		this.mapDao = daos.getMapDao();
		this.autoClose = autoClose;
	}

	public Long readLong(String sql) {

	    if ( SHOWSQL ) {
            log.info("readLong(sql):{}", sql);
        }

		try {
			return jdbcDao.readLong(sql);
		} catch (PersistenceException e) {
			throw new ButterflyDbException(e);
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
			throw new ButterflyDbException(e);
		} finally {
			autoCloseConnection();
		}

	}
	

	public Long readLong(String sql, Map paramMap) {

	    if ( SHOWSQL ) {
            log.info("readLong(sql, Map):{}, para:{}", sql, paramMap);
        }

		Tuple2<String,Object[]> sqlAndPara = toPreparedSqlAndPara(sql, paramMap);
		
		return readLong(sqlAndPara.e0,sqlAndPara.e1);

	}


	public String readIdString(String sql) {

	    if ( SHOWSQL ) {
            log.info("readIdString(sql]):{}", sql);
        }

		try {
			return jdbcDao.readIdString(sql);
		} catch (PersistenceException e) {
			throw new ButterflyDbException(e);
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
			throw new ButterflyDbException(e);
		} finally {
			autoCloseConnection();
		}

	}
	

	public String readIdString(String sql,  Map paramMap) {

	    if ( SHOWSQL ) {
            log.info("readIdString(sql, Map):{}, para:{}", sql, paramMap);
        }

		Tuple2<String,Object[]> sqlAndPara = toPreparedSqlAndPara(sql, paramMap);
		
		return readIdString(sqlAndPara.e0,sqlAndPara.e1);

	}
	

	public String readIdString(String sql, IPreparedStatementManager statementManager) {

	    if ( SHOWSQL ) {
            log.info("readIdString(sql,IPreparedStatementManager):{}", sql);
        }

		try {
			return jdbcDao.readIdString(sql, statementManager);
		} catch (PersistenceException e) {
			throw new ButterflyDbException(e);
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
			throw new ButterflyDbException(e);
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
			throw new ButterflyDbException(e);
		} finally {
			autoCloseConnection();
		}

	}
	

	public <T> T read(String sql, IResultSetProcessor processor,  Map paramMap) {

	    if ( SHOWSQL ) {
            log.info("read(sql,IResultSetProcessor,Map):{}, para:{}", sql, paramMap);
        }

		Tuple2<String,Object[]> sqlAndPara = toPreparedSqlAndPara(sql, paramMap);
		
		return read(sqlAndPara.e0,processor,sqlAndPara.e1);

	}

	public <T> T read(String sql, IPreparedStatementManager statementManager, IResultSetProcessor processor) {

	    if ( SHOWSQL ) {
            log.info("read(sql,IPreparedStatementManager,IResultSetProcessor):{}", sql);
        }

		try {
			return (T)jdbcDao.read(sql, statementManager, processor);
		} catch (PersistenceException e) {
			throw new ButterflyDbException(e);
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
			throw new ButterflyDbException(e);
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
			throw new ButterflyDbException(e);
		} finally {
			autoCloseConnection();
		}

	}
	

	public int update(String sql, Map paramMap) {
	    if ( SHOWSQL ) {
            log.info("update(sql, Map):{}, para:{}", sql, paramMap);
        }
		Tuple2<String,Object[]> sqlAndPara = toPreparedSqlAndPara(sql, paramMap);
		
		return update(sqlAndPara.e0,sqlAndPara.e1);
	}

	public int update(String sql, IPreparedStatementManager statementManager) {

	    if ( SHOWSQL ) {
            log.info("update(sql, IPreparedStatementManager):{}", sql);
        }

		try {
			return jdbcDao.update(sql, statementManager);
		} catch (PersistenceException e) {
			throw new ButterflyDbException(e);
		} finally {
			autoCloseConnection();
		}

	}
	
	
	
	
	
	
	
	

	
	
	//=====================================================//
	// mapDao
	
	
	private volatile RowDataProcessor rowDataProcessor;
	
	public ButterflyDao setRowDataProcessor(RowDataProcessor rowDataProcessor) {
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
			return CollectionUtil.isBlank( data ) ? null : (T)data.values().iterator().next();
			
		} catch (PersistenceException e) {
			throw new ButterflyDbException(e);
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
			return CollectionUtil.isBlank( data ) ? null : (T)data.values().iterator().next();

		} catch (PersistenceException e) {
			throw new ButterflyDbException(e);
		} finally {
			autoCloseConnection();
		}
	}
	

	public <T> T readObject(String sql, Map paramMap) {

        if ( SHOWSQL ) {
            log.info("readObject(sql, Map):{}, para:{}", sql, paramMap);
        }

		Tuple2<String,Object[]> sqlAndPara = toPreparedSqlAndPara(sql, paramMap);
		
		return readObject(sqlAndPara.e0,sqlAndPara.e1);
	}

	
	
	
	
	
	
	
	
	
	
	public Map<String,Object> readMap(String sql) {

	    if ( SHOWSQL ) {
            log.info("readMap(sql):{}", sql);
        }

		try {
			return dataProc( mapDao.readMap(sql) );
		} catch (PersistenceException e) {
			throw new ButterflyDbException(e);
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
			throw new ButterflyDbException(e);
		} finally {
			autoCloseConnection();
		}

	}	

	public Map<String,Object> readMap(String sql, Map paramMap) {

	    if ( SHOWSQL ) {
            log.info("readMap(sql, Map):{}, para:{}", sql, paramMap);
        }

		Tuple2<String,Object[]> sqlAndPara = toPreparedSqlAndPara(sql, paramMap);
		
		return readMap(sqlAndPara.e0,sqlAndPara.e1);

	}


	public Map<String,Object> readMap(String sql, IPreparedStatementManager statementManager) {

	    if ( SHOWSQL ) {
            log.info("readMap(sql, IPreparedStatementManager):{}", sql);
        }

		try {
			return dataProc( mapDao.readMap(sql, statementManager) );
		} catch (PersistenceException e) {
			throw new ButterflyDbException(e);
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
			throw new ButterflyDbException(e);
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
			throw new ButterflyDbException(e);
		} finally {
			autoCloseConnection();
		}

	}
	
	public List<Map<String,Object>> readMapList(String sql, Map paramMap) {

	    if ( SHOWSQL ) {
            log.info("readMapList(sql, Map):{}, para:{}", sql, paramMap);
        }

		Tuple2<String,Object[]> sqlAndPara = toPreparedSqlAndPara(sql, paramMap);
		
		return readMapList(sqlAndPara.e0,sqlAndPara.e1);

	}


	public List<Map<String,Object>> readMapList(String sql, IPreparedStatementManager statementManager) {

	    if ( SHOWSQL ) {
            log.info("readMapList(sql, IPreparedStatementManager):{}", sql);
        }

		try {
			return listProc( mapDao.readMapList(sql, statementManager) );
		} catch (PersistenceException e) {
			throw new ButterflyDbException(e);
		} finally {
			autoCloseConnection();
		}

	}
	
	
	
	
	

	
	
	
	
	
	
	
	
	private void autoCloseConnection() {
		if( this.autoClose ) {
			this.closeConnection();
		}
	}
	
	public void closeConnection() {
		try {
			this.daos.closeConnection();
		} catch (PersistenceException e) {					
			log.error("closeConnection", e);
		}
	}
	

}
