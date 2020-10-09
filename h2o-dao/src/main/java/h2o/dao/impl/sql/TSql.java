package h2o.dao.impl.sql;

import h2o.common.util.collection.MapBuilder;
import h2o.dao.DbUtil;
import h2o.dao.sql.SqlSource;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

public class TSql extends Sql implements SqlSource {	

	private Map<String,?> tdata;
	
	public TSql( String sql ) {
		super( sql );
	}
	

	public TSql setData(Map<String, Object> data) {
		this.tdata = data;
		return this;
	}
	


	@Override
	public String getSql( Map<String,Object> data ) {
		
		String sql = this.getSql();
		
		if( StringUtils.isBlank(sql) ) {
			return sql;
		}
		
		if( data == null ) {
			data = MapBuilder.newMap();
		}
		
		Map<String,Object> tSqldata = MapBuilder.newMap(); {

            Map<String, Object> templateData = DbUtil.newSqlTable().getTemplateData();
            if( templateData != null ) {
                tSqldata.putAll( templateData );
            }
            if( this.tdata != null ) {
                tSqldata.putAll(this.tdata);
            }
            tSqldata.putAll( data );

		}
		
		return DbUtil.sqlTemplateUtil.process( tSqldata , sql ) ;
	}

}
