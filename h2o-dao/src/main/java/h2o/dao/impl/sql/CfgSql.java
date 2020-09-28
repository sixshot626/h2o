package h2o.dao.impl.sql;

import h2o.common.util.lang.RuntimeUtil;
import h2o.dao.sql.SqlSource;
import org.apache.commons.lang.StringUtils;

public class CfgSql extends PathCfgSql implements SqlSource {
	
	
	private static String getSqlPath( Class<?> runClazz ) {
		
		String runClassName =  runClazz == null ? RuntimeUtil.getCallClassName( CfgSql.class.getName() ) : runClazz.getName();	
		if( runClassName == null ) {
			
			throw new RuntimeException("Get class path Exception ");
			
		} else {
			
			return StringUtils.replace( StringUtils.substringBeforeLast(runClassName, "$") , ".", "/");
		}
	}
	
	
	public CfgSql( String key ) {
		super( getSqlPath(null) , key );
	}
	

	
	public CfgSql( Class<?> runClazz ,  String key ) {
		super( getSqlPath(runClazz) , key );
	}
	
	
	
	public CfgSql( CfgSqlHelper helper ,  String key ) {
		super( helper.path , key );
	}

	
	
	public static CfgSqlHelper helper() {
		return new CfgSqlHelper( getSqlPath(null) );
	}
	
	public static CfgSqlHelper helper( Class<?> runClazz ) {
		return new CfgSqlHelper( getSqlPath(runClazz) );
	}
	
	
	
	public static class CfgSqlHelper {
		
		private final String path;		
		
		private CfgSqlHelper( String path ) {
			this.path = path;
		}
		
	}




}
