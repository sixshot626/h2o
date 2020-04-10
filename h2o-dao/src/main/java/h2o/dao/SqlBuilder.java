package h2o.dao;

import h2o.common.collections.builder.MapBuilder;
import h2o.dao.colinfo.ColInfo;
import h2o.dao.colinfo.ColInfoUtil;
import h2o.dao.exception.DaoException;
import h2o.dao.impl.sql.TSql;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;






public class SqlBuilder {

    private static final Logger log = LoggerFactory.getLogger( SqlBuilder.class.getName() );

    private volatile boolean isSilently = true;
	
	public void setSilently(boolean isSilently) {
		this.isSilently = isSilently;
	}
	

	public SqlSource buildInsertSql( Object bean  , String[] attrNames , String[] skipAttrNames  ) throws DaoException {
		return this.buildInsertSql(false, false, bean, attrNames , skipAttrNames );
	}

	
	public SqlSource buildInsertSqlIncludeNull( Object bean  ,  String[] attrNames , String[] skipAttrNames ) throws DaoException {
		return this.buildInsertSql(true, false, bean, attrNames , skipAttrNames);
	}

	
	public SqlSource buildAllInsertSqlIncludeNull( Object bean  , String[] attrNames , String[] skipAttrNames ) throws DaoException {
		return this.buildInsertSql(true, true, bean, attrNames , skipAttrNames );
	}


	public SqlSource buildInsertSql( boolean includeNull , boolean isAllattr , Object bean  , String[] attrNames , String[] skipAttrNames ) throws DaoException {
		
		String tabName = ColInfoUtil.getTableName(bean);		
		List<ColInfo> colInfos = ColInfoUtil.getColInfoInAttrNames( bean , isAllattr , attrNames , skipAttrNames , this.isSilently);		
		if(colInfos == null ) {			
			return null;		
		}
		
		Map<String,ColInfo> colInfoMap = MapBuilder.newMap();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(" INSERT INTO " );
		sb.append(tabName);
		sb.append(" (");	
		
		
		
		StringBuilder sbv = new StringBuilder();
		sbv.append(") VALUES (");
		
		int i = 0;
		for( ColInfo ci :  colInfos ) {
			
			colInfoMap.put( ci.attrName , ci );
			
			Object val =getVal( bean , ci.attrName );
					
			if( !includeNull && val == null && ci.defVal == null) {
				continue;
			}
			
			if( i++ > 0 ) {
				sb.append(",");
				sbv.append(",");
			}
			sb.append(ci.colName);			
			sbv.append( ci.value( val == null ) );
			
		}
		
		if( i == 0 ) {
			if(this.isSilently) {
				return null;
			} else {
				throw new DaoException( "None not null attr");
			}
		}
		
		sbv.append(")");
		
		String sql = sb.append(sbv).toString();
		
		log.debug("buildInsertSql ======= <{}>" , sql);
		
		TSql tSql = new TSql(sql);
		tSql.setData( MapBuilder.so().put("_COL_INFO", colInfoMap).get() );
		
		return tSql;
		
	}
	

	
	public SqlSource buildUpdateSql( Object bean  , String where , String[] attrNames , String[] skipAttrNames  ) throws DaoException {
		return this.buildUpdateSql(false, false, bean, where , attrNames , skipAttrNames );
	}

	
	public SqlSource buildAllUpdateSql( Object bean  , String where , String[] attrNames , String[] skipAttrNames ) throws DaoException {
		return this.buildUpdateSql(false, true, bean, where , attrNames , skipAttrNames );
	}

	
	public SqlSource buildUpdateSqlIncludeNull( Object bean  , String where , String[] attrNames , String[] skipAttrNames ) throws DaoException {
		return this.buildUpdateSql(true, false, bean, where , attrNames , skipAttrNames );
	}

	
	public SqlSource buildAllUpdateSqlIncludeNull( Object bean  , String where , String[] attrNames , String[] skipAttrNames  ) throws DaoException {
		return this.buildUpdateSql(true, true, bean, where , attrNames , skipAttrNames);
	}


	public SqlSource buildUpdateSql( boolean includeNull , boolean isAllattr , Object bean ,  String where  , String[] attrNames , String[] skipAttrNames  ) throws DaoException {
		
		
		String tabName = ColInfoUtil.getTableName(bean);		
		List<ColInfo> colInfos = ColInfoUtil.getColInfoInAttrNames(bean , isAllattr , attrNames , skipAttrNames , this.isSilently);		
		if(colInfos == null ) {			
			return null;		
		}
		
		Map<String,ColInfo> colInfoMap = MapBuilder.newMap();
		
		StringBuilder sb = new StringBuilder();
		sb.append(" UPDATE " );
		sb.append(tabName);
		sb.append(" SET ");	
		int i = 0;
		for( ColInfo ci :  colInfos ) {
			
			colInfoMap.put( ci.attrName , ci );
			
			Object val =getVal( bean , ci.attrName );
						
			if( !includeNull && val == null && ci.defVal == null ) {
				continue;
			}
			
			if( i++ > 0 ) {
				sb.append(",");
			}
			sb.append(ci.colName);
			sb.append("=");			
			sb.append( ci.value( val == null ) );
			
		}
		
		if( i == 0 ) {
			if(this.isSilently) {
				return null;
			} else {
				throw new DaoException( "none not null attr");
			}
		}
		
		sb.append(" ");
		if( StringUtils.isNotBlank(where) ) {
			sb.append("where ");
			sb.append(where);
		}
		
		String sql = sb.toString();
		
		log.debug("buildUpdateSql ======= <{}>" , sql);
		
		TSql tSql = new TSql(sql);
		tSql.setData( MapBuilder.so().put("_COL_INFO", colInfoMap).get() );
		
		return tSql;
		
	}
	
	
	private Object getVal( Object bean , String attrName ) {
		return h2o.jodd.bean.BeanUtil.silent.getProperty(bean, attrName);
	}
	
	
	

}
