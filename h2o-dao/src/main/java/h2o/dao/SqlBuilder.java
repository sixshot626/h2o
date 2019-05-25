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
	

	
	
	public SqlSource buildInsertSql( Object bean  , String... attrNames ) throws DaoException {
		return this.buildInsertSql(false, false, bean, (String[])attrNames);
	}
	
	public SqlSource buildInsertSql2( Object bean  , String[] attrNames , String[] skipAttrNames  ) throws DaoException {
		return this.buildInsertSql2(false, false, bean, attrNames , skipAttrNames );
	}
	
	public SqlSource buildInsertSql3( Object bean  ,  String... skipAttrNames  ) throws DaoException {
		return this.buildInsertSql2(false, false, bean, (String[])null , (String[])skipAttrNames );
	}
	
	
	
	
	
	public SqlSource buildAllInsertSql( Object bean  , String... attrNames ) throws DaoException {
		return this.buildInsertSql(false, true, bean, (String[])attrNames);
	}
	
	public SqlSource buildAllInsertSql2( Object bean  , String[] attrNames , String[] skipAttrNames ) {
		return this.buildInsertSql2(false, true, bean, attrNames , skipAttrNames);
	}
	

	
	
	public SqlSource buildInsertSqlIncludeNull( Object bean  , String... attrNames ) throws DaoException {
		return this.buildInsertSql(true, false, bean, (String[])attrNames);
	}
	
	public SqlSource buildInsertSqlIncludeNull2( Object bean  ,  String[] attrNames , String[] skipAttrNames ) throws DaoException {
		return this.buildInsertSql2(true, false, bean, attrNames , skipAttrNames);
	}
	
	public SqlSource buildInsertSqlIncludeNull3( Object bean  ,  String... skipAttrNames ) throws DaoException {
		return this.buildInsertSql2(true, false, bean, (String[])null , (String[])skipAttrNames);
	}
	
	
	
	
	
	
	public SqlSource buildAllInsertSqlIncludeNull( Object bean  , String... attrNames ) throws DaoException {
		return this.buildInsertSql(true, true, bean, (String[])attrNames);
	}
	
	public SqlSource buildAllInsertSqlIncludeNull2( Object bean  , String[] attrNames , String[] skipAttrNames ) throws DaoException {
		return this.buildInsertSql2(true, true, bean, attrNames , skipAttrNames );
	}

	
	
	

	public SqlSource buildInsertSql( boolean includeNull , boolean isAllattr , Object bean  , String... attrNames ) throws DaoException {
		return this.buildInsertSql2(includeNull, isAllattr, bean, attrNames, (String[])null );
	}	
	
	public SqlSource buildInsertSql2( boolean includeNull , boolean isAllattr , Object bean  , String[] attrNames , String[] skipAttrNames ) throws DaoException {
		
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
	
	
	
	
	
	
	
	
	
	
	public SqlSource buildUpdateSql( Object bean  , String where , String... attrNames ) throws DaoException {
		return this.buildUpdateSql(false, false, bean, where , (String[])attrNames);
	}
	
	public SqlSource buildUpdateSql2( Object bean  , String where , String[] attrNames , String[] skipAttrNames  ) throws DaoException {
		return this.buildUpdateSql2(false, false, bean, where , attrNames , skipAttrNames );
	}
	
	public SqlSource buildUpdateSql3( Object bean  , String where , String... skipAttrNames  ) throws DaoException {
		return this.buildUpdateSql2(false, false, bean, where , (String[])null , (String[])skipAttrNames );
	}
	
	
	
	
	
	
	
	public SqlSource buildAllUpdateSql( Object bean  , String where , String... attrNames ) throws DaoException {
		return this.buildUpdateSql(false, true, bean, where , (String[])attrNames);
	}
	
	public SqlSource buildAllUpdateSql2( Object bean  , String where , String[] attrNames , String[] skipAttrNames ) throws DaoException {
		return this.buildUpdateSql2(false, true, bean, where , attrNames , skipAttrNames );
	}

	
	
	
	public SqlSource buildUpdateSqlIncludeNull( Object bean  , String where , String... attrNames ) throws DaoException {
		return this.buildUpdateSql(true, false, bean, where , (String[])attrNames);
	}
	
	public SqlSource buildUpdateSqlIncludeNull2( Object bean  , String where , String[] attrNames , String[] skipAttrNames ) throws DaoException {
		return this.buildUpdateSql2(true, false, bean, where , attrNames , skipAttrNames );
	}
	
	public SqlSource buildUpdateSqlIncludeNull3( Object bean  , String where , String... skipAttrNames ) throws DaoException {
		return this.buildUpdateSql2(true, false, bean, where , (String[])null , (String[])skipAttrNames );
	}
	
	
	
	
	public SqlSource buildAllUpdateSqlIncludeNull( Object bean  , String where , String... attrNames ) throws DaoException {
		return this.buildUpdateSql(true, true, bean, where , (String[])attrNames);
	}
	
	public SqlSource buildAllUpdateSqlIncludeNull2( Object bean  , String where , String[] attrNames , String[] skipAttrNames  ) throws DaoException {
		return this.buildUpdateSql2(true, true, bean, where , attrNames , skipAttrNames);
	}

	
	
	
	public SqlSource buildUpdateSql( boolean includeNull , boolean isAllattr , Object bean ,  String where  , String... attrNames ) throws DaoException {		
		return this.buildUpdateSql2(includeNull, isAllattr, bean, where, attrNames, (String[])null );
	}
	
	
	public SqlSource buildUpdateSql2( boolean includeNull , boolean isAllattr , Object bean ,  String where  , String[] attrNames , String[] skipAttrNames  ) throws DaoException {	
		
		
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
