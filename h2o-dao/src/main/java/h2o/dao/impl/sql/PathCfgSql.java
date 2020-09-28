package h2o.dao.impl.sql;

import h2o.dao.DbUtil;
import h2o.dao.sql.SqlSource;

public class PathCfgSql extends TSql implements SqlSource {	
	
	public PathCfgSql( String path ,  String key ) {
		super( DbUtil.newSqlTable().getSql( path , key) );
	}	

}
