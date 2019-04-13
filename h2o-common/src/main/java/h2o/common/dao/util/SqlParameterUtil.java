package h2o.common.dao.util;

import h2o.common.collections.CollectionUtil;
import h2o.common.collections.builder.ListBuilder;
import h2o.common.collections.builder.MapBuilder;
import h2o.common.collections.tuple.Tuple2;
import h2o.common.collections.tuple.TupleUtil;
import h2o.common.dao.util.namedparam.NamedParameterUtils;
import h2o.common.dao.util.namedparam.SqlParameterInfo;
import h2o.common.util.bean.BeanUtil;
import h2o.common.util.bean.support.JoddBeanUtilVOImpl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SqlParameterUtil {
	
	private final BeanUtil beanUtil;
	
	public SqlParameterUtil() {
		this.beanUtil = new BeanUtil( new JoddBeanUtilVOImpl(true,false) , null );
	}
	
	
	public SqlParameterUtil( BeanUtil beanUtil ) {
		this.beanUtil = beanUtil;
	}
	

	public Map<String,Object> toMap( Object... args ) {
		
		Map<String,Object> m = MapBuilder.newMap();
		
		if( !CollectionUtil.argsIsBlank(args) ) for(int i = 0; i < args.length ; i++ ) {
			
			Object a = args[i];
			
			if( a == null ) {
				continue;
			}
			
			if( otherToMapProc( a , m ) ) {
			} else if( a instanceof Map ) {
				
				for( Map.Entry<?, ?> e : ( (Map<?, ?>)a ).entrySet()) {
					m.put( e.getKey().toString() , valConvert(e.getValue()) );
				}
				
			} else if( a instanceof String ) {
				
				Object val = args[++i];
				
				m.put( (String)a , valConvert(val) );
				
			} else {
				
				Map<String, Object> sqlPara = beanUtil.bean2Map(a);
				for( Map.Entry<String, Object> e : sqlPara.entrySet()) {
					m.put( e.getKey() , valConvert(e.getValue()) );
				}
			}	
			
		}
		
		return m;
		
	}
	
	@SuppressWarnings("rawtypes")
	protected Object valConvert( Object val ) {

        if( val == null ) {

            return null;

        } else if( val instanceof Enum ) {

            return ((Enum)val).name();

        } else if( val instanceof List ) {

            List<Object> ll = (List)val;
            List<Object> nl = ListBuilder.newList(ll.size());

            for( Object l : ll ) {
                nl.add( valConvert(l) );
            }
            return nl;

        } else {

            return val;
        }

	}
	
	
	protected boolean otherToMapProc( Object arg , Map<String,Object> m ) {
		return false;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static Tuple2<String,Object[]> toPreparedSqlAndPara( String sql, Map paramMap ) {
		
		SqlParameterInfo sqlAndPara = NamedParameterUtils.mapPara2ObjectsPara(sql, paramMap);
		
		List<Object> para = ListBuilder.newList();
		for( Object value : sqlAndPara.getParams() ) {
			if (value instanceof Collection) {
				Iterator entryIter = ((Collection) value).iterator();
				
				while (entryIter.hasNext()) {
					
					Object entryItem = entryIter.next();
					if (entryItem instanceof Object[]) {
						Object[] expressionList = (Object[]) entryItem;
						
						for (int m = 0; m < expressionList.length; m++) {							
							para.add(expressionList[m]);
						}
						
					}
					else {
						para.add(entryItem);
					}
				}
			}
			else {
				para.add(value);
			}
		}
		
		return TupleUtil.t( sqlAndPara.getSql() , para.toArray() );
		
	}
	
	
	
	
}
