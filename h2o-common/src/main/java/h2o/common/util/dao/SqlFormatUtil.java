package h2o.common.util.dao;

import h2o.common.util.bean.PreOperate;
import h2o.common.util.collections.CollectionUtil;
import h2o.common.util.dao.util.StringSingleQuoteEscape;
import org.apache.commons.lang.StringUtils;

public class SqlFormatUtil {
	
	private SqlFormatUtil() {}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String toString( Object o , PreOperate...  ops ) {		
		
		Object s = o;
		
		if( !CollectionUtil.java5ArgsIsBlank(ops)  ) {
			for( PreOperate op : ops ) {
				s = op.doOperate(s);
			}
		}
		
		return s == null ? "null" : "'" + s.toString() + "'";
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String toNumber( Object o , PreOperate...  ops ) {		
		
		Object s = o;
		
		if( !CollectionUtil.java5ArgsIsBlank(ops)  ) {
			for( PreOperate op : ops ) {
				s = op.doOperate(s);
			}
		}
		
		return s == null || StringUtils.isBlank(String.valueOf(s))  ? "null" : String.valueOf(s) ;
	}
	
	
	private static  StringSingleQuoteEscape eop = new StringSingleQuoteEscape();
	
	public static String toEscapedString( Object o ) {		
		return toString(o,eop);
	}
	
	@SuppressWarnings("unchecked")
	public static String escape( Object o ) {
		return (String)eop.doOperate(o);
	}


}
