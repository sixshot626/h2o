package h2o.dao;


import h2o.common.collections.builder.MapBuilder;
import h2o.common.dao.SqlTable;
import h2o.common.thirdparty.freemarker.TemplateUtil;
import h2o.dao.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Map;

public final class DbUtil {

    private static final Logger log = LoggerFactory.getLogger( DbUtil.class.getName() );

    public static final String DEFAULT_DATASOURCE_NAME = "default";

    public static final DBFactory DBFACTORY = DBFactoryProvider.getDbFactory();

	public static final SqlTable sqlTable = newSqlTable();

	public static SqlTable newSqlTable() {
		return DBFACTORY.getSqlTable();
	}


	public static final SqlBuilder 		sqlBuilder 			= DBFACTORY.getSqlBuilder();
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

	public static Db getDb( String dsName ){
		return DBFACTORY.getDb( dsName );
	}



	public static Dao getDao(){
		return getDb().getDao();
	}

	public static Dao getDao( String dsName ){
		return getDb(dsName).getDao();
	}

}
