package h2o.common.collections;

import h2o.common.collections.builder.ListBuilder;
import org.apache.commons.lang.StringUtils;

import java.util.*;



public class CollectionUtil {

	private CollectionUtil() {}
	
	
	
	public static boolean argsIsBlank( Object[] args ) {
		return args == null || args.length == 0 || ( args.length == 1 && args[0] == null);
	}
	
	public static boolean isBlank( Collection<?> c ) {
		return c == null || c.isEmpty();
	}
	
	public static boolean isNotBlank( Collection<?> c ) {
		return c != null && !c.isEmpty();
	}
	
	public static boolean isBlank( Map<?,?> m ) {
		return m == null || m.isEmpty();
	}
	
	public static boolean isNotBlank( Map<?,?> m  ) {
		return m != null && !m.isEmpty();
	}
	

	@SuppressWarnings("rawtypes")
	public static String toString(Collection c) {
		return toString(c , false);
	}

	@SuppressWarnings("rawtypes")
	public static String toString(Collection c, boolean is_string) {
		if (c == null || c.isEmpty()) {
			return null;
		}

		StringBuilder r = new StringBuilder();
		Iterator itr = c.iterator();
		while (itr.hasNext()) {
			String id = itr.next().toString();
			if (is_string) {
				r.append(",'");
				r.append(id);
				r.append("'");
			} else {
				r.append(",");
				r.append(id);
			}
		}

		return r.substring(1);
	}


	public static List<String> all2List( String str, String[] tns , String def) {
		return toList(true, str, tns , def);
	}
	
	public static List<String> all2List(  String str, String... tns) {
		return toList(true, str, tns, "");
	}

	public static List<String> toList(  String str, String... tns) {
		return toList(false, str, tns, "");
	}

	private static List<String> toList( boolean isAll , String str ,  String[] tns , String def) {

		ArrayList<String> r = new ArrayList<String>();
		r.add(str);
		
		if(argsIsBlank(tns)) {
			tns = new String[] { null };
		}
		
		for( String tn : tns ) {
			ArrayList<String> r2 = new ArrayList<String>();
			
			
			for( String sr : r ) {
				
				if( sr == null || "".equals(sr) ) {
					if( isAll ) {
						r2.add(def);
					}
					
				} else {
					
					String[] ss = StringUtils.splitByWholeSeparatorPreserveAllTokens(sr, tn);
					
					for( String s : ss ) {
						if(s == null || "".equals(s)) {
							if( isAll ) {
								r2.add(def);
							}
						} else {
							r2.add(s);
						}
						
					}
				}
			}
			
			r = r2;
			
		}
		
		return r;		

	}


	@SuppressWarnings("rawtypes")
	public static <E> List<E>[] split( Collection<E> c , int length ) {

		if ( length <= 0 ) {
			throw new IllegalArgumentException( "length" );
		}

		List<Object> rls = ListBuilder.newList();

		int n = 0;
		List<E> ltmp = ListBuilder.newList( length );
		for ( E e : c ) {
			ltmp.add( e );
			if ( ++n == length ) {
				rls.add( ltmp );
				ltmp = ListBuilder.newList( length );
				n = 0;
			}
		}

		if ( n > 0 ) {
			rls.add( ltmp );
		}

		return rls.toArray( (List<E>[]) new List[ rls.size() ]  );

	}


	

}
