package h2o.common.util.math;

import h2o.common.thirdparty.spring.util.NumberUtils;

import java.math.BigInteger;

public final class IntArith {
	
	private IntArith() {}
	
	public static <T> T add( T v1, T v2) {

		BigInteger b1 = toBigInteger(v1);

		BigInteger b2 = toBigInteger(v2);

		return toOtherType(b1.add(b2),v1);

	}
	
	public static <T> T sub( T v1, T v2) {

		BigInteger b1 = toBigInteger(v1);

		BigInteger b2 = toBigInteger(v2);

		return toOtherType( b1.subtract(b2) , v1 );

	}
	
	public static <T> T mul(T v1, T v2) {

		BigInteger b1 = toBigInteger(v1);
		BigInteger b2 = toBigInteger(v2);

		return toOtherType( b1.multiply(b2) , v1 );

	}
	
	
	public static <T> T div( T v1, T v2) {		

		BigInteger b1 = toBigInteger(v1);

		BigInteger b2 = toBigInteger(v2);

		return toOtherType( b1.divide(b2) , v1 );

	}
	
	
	public static <T> int compareTo( T v1, T v2 ) {

		BigInteger b1 = toBigInteger(v1);

		BigInteger b2 = toBigInteger(v2);

		return b1.compareTo(b2);

	}
	
	
	public static BigInteger toBigInteger( Object o ) {
		if( o == null) {
			throw new NullPointerException();
		}
		if( o instanceof BigInteger ) {
			return (BigInteger)o;
		} 
		
		return (new BigInteger(o.toString()));
		
	}
	

	@SuppressWarnings("unchecked")
	public static <T> T toOtherType( BigInteger b , T t) {
		
		
		if( t instanceof BigInteger ) {
			return (T)b; 
		}
		if( t instanceof String ) {		
			return (T)(b.toString());		 
		} 
		if( t instanceof Number ) {
			return (T)NumberUtils.convertNumberToTargetClass(b, t.getClass());
		}
		
		throw new RuntimeException(" Type mismatch . ");
		
	}
	
	

}
