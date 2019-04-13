package h2o.dao.impl.orm;

import h2o.common.Tools;
import h2o.common.collections.builder.MapBuilder;
import h2o.common.util.bean.BeanUtil;
import h2o.dao.colinfo.ColInfo;
import h2o.dao.colinfo.ColInfoUtil;

import java.util.List;
import java.util.Map;

public class DbMap2BeanProcessor {

	private final BeanUtil beanUtil;

	private final Map<String,String> colAttrMap;



	public DbMap2BeanProcessor( Class<?> beanClazz ) {
		this( beanClazz , Tools.bic );
	}
	
	public DbMap2BeanProcessor( Class<?> beanClazz , BeanUtil beanUtil ) {
	
		
		Map<String,String> caMap = null;
		if( ColInfoUtil.hasTableAnnotation( beanClazz ) ) {
			
			caMap = MapBuilder.newMap();
			
			List<ColInfo> cis = ColInfoUtil.getColInfos( beanClazz );
			for( ColInfo ci : cis ) {
				caMap.put( ci.attrName , ci.colName );
			}
			
		}
		
		colAttrMap = caMap;
		this.beanUtil = beanUtil;
				
	}

	public <T> T toBean( Map<?, ?> m , T bean) {

		if( colAttrMap == null ) {
			return beanUtil.beanCopy(m, bean );
		}
		
		String[] prepNames = beanUtil.analysePrepNames(bean);
		String[] srcpNames = new String[ prepNames.length ];
		
		for( int i = 0 ; i < prepNames.length ; i++ ) {
			String colName = colAttrMap.get( prepNames[i] );
			srcpNames[i] = colName == null ? prepNames[i] : colName;
		}	
		
		
		return beanUtil.beanCopy(m, bean , srcpNames, prepNames );
		
	}



	
	
	
}
