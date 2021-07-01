package h2o.dao.column;

import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.collection.ListBuilder;
import h2o.dao.annotation.*;
import h2o.dao.exception.DaoException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

public class ColumnMetaUtil {

    private static final Logger log = LoggerFactory.getLogger( ColumnMetaUtil.class.getName() );

    private ColumnMetaUtil() {}

	public static boolean hasTableAnnotation( Object bean ) {
		
		if( bean == null ) {
			return false;
		}
		
		boolean isClass = (bean instanceof Class);
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Class<Object> beanClass = isClass ? (Class<Object>)bean : (Class<Object>) bean.getClass();
		
		return beanClass.getAnnotation(Table.class) != null;
	}
	
	
	
	public static String getTableName( Object bean ) {
		
		if( bean == null ) {
			return null;
		}
		
		
		boolean isClass = (bean instanceof Class);
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Class<Object> beanClass = isClass ? (Class<Object>)bean : (Class<Object>) bean.getClass();

		Table tableAnn = beanClass.getAnnotation(Table.class);
		
		if( tableAnn != null && !StringUtils.isBlank(tableAnn.name())) {
			return tableAnn.name();
		} else {
			return bean.getClass().getSimpleName();
		}
		
	}
	
	public static List<ColumnMeta> getColInfoInAttrNames(Object bean , boolean isAllAttr , String[] attrNames , String[] skipAttrNames , boolean isSilently ) throws DaoException  {
		
		List<ColumnMeta> columnMetas = getColInfos(bean);
		if(columnMetas.isEmpty()) {
			if( isSilently ) {
				return null;
			} else {
				throw new DaoException( "ColInfos is empty");
			}
		}
		
		
		if(!CollectionUtil.argsIsBlank(attrNames)) {
			
			Map<String,String> defValMap = new HashMap<String,String>();
			Set<String> attrNameSet = new HashSet<String>();
			
			for( String attr : attrNames) {
				int idx = attr.indexOf('=');
				if( idx != -1 ) {
					String nAttr = attr.substring(0,idx);
					defValMap.put( nAttr , attr.substring(idx + 1 , attr.length()));
					attrNameSet.add(nAttr);
				} else {
					attrNameSet.add(attr);
				}
			}
			
			
			if(!isAllAttr) {				
				
				List<ColumnMeta> colInfosTmp = new ArrayList<ColumnMeta>();
				for( ColumnMeta ci : columnMetas) {
					if( attrNameSet.contains(ci.attrName) ) {
						colInfosTmp.add(ci);
					}		
					
				}
				
				if(colInfosTmp.isEmpty()) {
					if( isSilently ) {
						return null;
					} else {
						throw new DaoException( "ColInfos in attrNames is empty");
					}
				} else {
					columnMetas = colInfosTmp;
				}
			
			}		
			

			List<ColumnMeta> defColumnMetas = ListBuilder.newList( columnMetas.size() );
			for( ColumnMeta ci : columnMetas) {
				if( defValMap.containsKey(ci.attrName) ) {
				    ColumnMetaBuilder vci = new ColumnMetaBuilder( ci );
                    vci.defVal = defValMap.get(ci.attrName);
                    defColumnMetas.add( vci.get() );
				} else {
				    defColumnMetas.add( ci );
                }
				
			}
			columnMetas = defColumnMetas;
					
			
			
		}
		
		
		if(!CollectionUtil.argsIsBlank(skipAttrNames)) {
			
			List<String> skipAttrNameList = Arrays.asList(skipAttrNames);
			
			List<ColumnMeta> skipColumnMetas = new ArrayList<ColumnMeta>();
			for( ColumnMeta ci : columnMetas) {
				if( ! skipAttrNameList.contains(ci.attrName) ) {
					skipColumnMetas.add(ci);
				}				
			}
			
			columnMetas = skipColumnMetas;
			
		}
		
		log.debug("colInfos ===== {}" , columnMetas);
		
		return columnMetas;
		
	}
	
	public static List<ColumnMeta> getColInfos(Object bean ) {
		
		boolean isClass = (bean instanceof Class);
		
		@SuppressWarnings("rawtypes")
		Class beanClass = isClass ? (Class)bean : bean.getClass();
		
		List<ColumnMeta> columnMetas = new ArrayList<ColumnMeta>();
		
		Field[] fs = h2o.jodd.util.ClassUtil.getSupportedFields(beanClass);
		for( Field f : fs ) {
			
			Column colAnn = f.getAnnotation(Column.class);			
			if( colAnn == null ) {
				continue;
			}
			
			ColumnMetaBuilder ci = new ColumnMetaBuilder();
			
			String fieldName = f.getName();
			
			ci.attrName =  colAnn.attrName();
			if( StringUtils.isBlank(ci.attrName) ) {
				ci.attrName = fieldName;
			}
			
			String colName = colAnn.name();
			ci.colName =  StringUtils.isBlank( colName ) ? fieldName : colName;

            DefaultValue dv = f.getAnnotation( DefaultValue.class );
            if ( dv != null ) {
                ci.defVal = dv.value();
            }

            PK id = f.getAnnotation( PK.class );
            if( id != null ) {
                ci.pk = true;
            }

            Unique unique = f.getAnnotation( Unique.class );
            if( unique != null ) {
                ci.uniqueNames = unique.value();
            }
			
			columnMetas.add(ci.get());
		}
		
		
		return columnMetas;
	}
	
	

}
