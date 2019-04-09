package h2o.common.util.bean;

import h2o.common.Tools;

import java.util.Collection;
import java.util.Map;

public class Beanfilter {
	
	
	private volatile BeanUtil bu = Tools.b;
	
	public void setBeanUtil(BeanUtil bu) {
		this.bu = bu;
	}
	
	private static Beanfilter bf = new Beanfilter();
	
	



	private Map<String,Object> filterBean( Object bean , Map<String,String[][]> filter) {		
		String[][] k = filter.get(bean.getClass().getName());	
		
		if( k == null ) {
			return bu.javaBean2Map(bean);
		}
		
		if( k[0] == null ) {
			return  bu.javaBean2Map(bean , k[1] , k[2]);
		}
		
		if( k[2] == null ) {
			return  bu.javaBean2Map3(bean , k[0] , null , null);
		}
		
		return  bu.javaBean2Map3(bean , k[0] , k[1] , k[2]);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object filterMap( Object bean , Map<String,String[][]> filter) {
		Map<Object,Object> m = (Map<Object,Object>) bean;
		for( Map.Entry e : m.entrySet() ) {
			Object v = e.getValue();
			if( !this.isBaseType(v) ) {
				e.setValue(this.filterObject(v, filter));
			}
		}
		return bean;
	}
	

	private Object filterArray( Object a , Map<String,String[][]> filter) {
		Object[] ss = (Object[]) a;
		Object[] os = new Object[ss.length];
		System.arraycopy(ss, 0, os, 0, ss.length);
		
		return this.filterArray0(os, filter);
	}
	
	private Object filterArray0( Object a , Map<String,String[][]> filter) {
		Object[] os = (Object[]) a;
		for( int i = 0 ; i < os.length ; i++ ) {
			if( !this.isBaseType(os[i]) ) {
				os[i] = this.filterObject(os[i], filter);
			}
		}
		return os;
	}
	
	@SuppressWarnings("rawtypes")
	private Object filterCollection( Object c , Map<String,String[][]> filter) {
		return this.filterArray0( ((Collection) c).toArray() , filter);
	}
	
	public Object filterObject( Object o , Map<String,String[][]> filter) {	
		
		if( this.isBaseType(o)) {
			return o;
		}
		
		if( o.getClass().isArray() ) {
			return this.filterArray(o, filter);
		} 
		
		if( o instanceof Collection ) {
			return this.filterCollection(o, filter);
		}
		
		if( o instanceof Map ) {
			return this.filterMap(o, filter);
		}
		
		return this.filterMap(this.filterBean(o,filter), filter);
		
	}
	
	private boolean isBaseType( Object o ) {
		if( o == null || o instanceof String || o instanceof Number || o instanceof Boolean) {
			return true;
		}
		return false;
	}
	
	
	public static Object filter( Object o , Map<String,String[][]> filter) {	
		return bf.filterObject(o, filter);
	}


}
