package h2o.dao;


import h2o.common.exception.ExceptionUtil;
import h2o.common.thirdparty.freemarker.TemplateUtil;
import h2o.common.util.collection.MapBuilder;
import h2o.dao.exception.DaoException;
import h2o.dao.sql.SqlBuilder;
import h2o.dao.sql.SqlTable;
import h2o.dao.transaction.JdbcTransactionManager;
import h2o.dao.transaction.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Map;
import java.util.function.Function;

public final class DbUtil {

    private static final Logger log = LoggerFactory.getLogger( DbUtil.class.getName() );

    public static final String DEFAULT_DATASOURCE_NAME = "default";

    public static final DBFactory DBFACTORY = DBFactoryProvider.getDbFactory();

	public static final SqlTable sqlTable = newSqlTable();

	public static SqlTable newSqlTable() {
		return DBFACTORY.getSqlTable();
	}


	public static final SqlBuilder      sqlBuilder 			= DBFACTORY.getSqlBuilder();
	public static final TemplateUtil 	sqlTemplateUtil 	= DBFACTORY.getSqlTemplateUtil();


	private static class S {
		public static final DbUtil dbUtil = DBFACTORY.getDbUtil();
	}


	private final Map<String,DataSource> dsMap = MapBuilder.newConcurrentHashMap();

	public void setDataSources( Map<String,DataSource> dataSources ) {
		dsMap.putAll(dataSources);
	}

	public void addDataSource( String name , DataSource ds ) {
		dsMap.put( name , ds );
	}



	public static DataSource getDataSource() {
		return getDataSource(DEFAULT_DATASOURCE_NAME);
	}

	public static DataSource getDataSource(String name) {

		log.debug(" getDataSource('{}') ... " , name );

		DataSource ds =  S.dbUtil.dsMap.get( name );
		if( ds == null ) {
			throw new DaoException( "DataSource [" + name + "] undefined." );
		}

		return ds;
	}



	public static Db getDb(){
		return getDb(DEFAULT_DATASOURCE_NAME);
	}

	public static Db getDb( String name ){
		return DBFACTORY.getDb( name );
	}


	public static Dao getDao(){
		return getDb().getDao();
	}

	public static Dao getDao( String name ){
		return getDb(name).getDao();
	}


	public static  <T> T qx( String name, Function<Dao, T> daoCallback) {

		Dao dao = null;

		try {
			dao = getDao(name);
			return daoCallback.apply( dao );

		} catch (Exception e) {

			log.debug("doCallback",e);
			throw ExceptionUtil.toRuntimeException(e);

		} finally {
			if ( dao != null ) {
				try {
					dao.close();
				} catch (Exception e) {}
			}
		}
	}


	public static  <T> T tx( String name, Function<Dao, T> txCallback ) {
		return tx( new JdbcTransactionManager(name) , txCallback );
	}

	public static  <T> T tx(TransactionManager txManager, Function<Dao, T> txCallback) {

		Dao dao = null;

		try {

			dao = txManager.getDao();

			T t = txCallback.apply( dao );

			txManager.commit();

			return t;

		} catch (Exception e) {

			log.debug("doCallback",e);

			txManager.rollback();

			throw ExceptionUtil.toRuntimeException(e);

		} finally {

			if ( dao != null ) {
				try {
					dao.close();
				} catch ( Exception e ) {}
			}

			if ( txManager instanceof AutoCloseable ) {
				try {
					((AutoCloseable) txManager).close();
				} catch ( Exception e ) {}
			}

		}
	}

}
