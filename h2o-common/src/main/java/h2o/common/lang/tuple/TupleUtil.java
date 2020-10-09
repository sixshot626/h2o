package h2o.common.lang.tuple;

public final class TupleUtil {
	
	private TupleUtil() {}
	
	public static <A,B> Tuple2<A,B> t2( A e0 , B e1 ) {
		return new Tuple2<A,B>(e0,e1);
	}
	
	public static <A,B,C> Tuple3<A,B,C> t3( A e0 , B e1 , C e2 ) {
		return new Tuple3<A,B,C>(e0,e1,e2);
	}
	
	public static <A,B,C,D> Tuple4<A,B,C,D> t4( A e0 , B e1 , C e2 , D e3 ) {
		return new Tuple4<A,B,C,D>(e0,e1,e2,e3);
	}
	
	public static <A,B,C,D,E> Tuple5<A,B,C,D,E> t5( A e0 , B e1 , C e2 , D e3 , E e4 ) {
		return new Tuple5<A,B,C,D,E>(e0,e1,e2,e3,e4);
	}
	
	public static <A,B,C,D,E,F> Tuple6<A,B,C,D,E,F> t6( A e0 , B e1 , C e2 , D e3 , E e4 , F e5 ) {
		return new Tuple6<A,B,C,D,E,F>(e0,e1,e2,e3,e4,e5);
	}
	
	public static <A,B> Tuple2<A,B> t( A e0 , B e1 ) {
		return t2(e0,e1);
	}
	
	public static <A,B,C> Tuple3<A,B,C> t( A e0 , B e1 , C e2 ) {
		return t3(e0,e1,e2);
	}
	
	public static <A,B,C,D> Tuple4<A,B,C,D> t( A e0 , B e1 , C e2 , D e3 ) {
		return t4(e0,e1,e2,e3);
	}
	
	public static <A,B,C,D,E> Tuple5<A,B,C,D,E> t( A e0 , B e1 , C e2 , D e3 , E e4 ) {
		return t5(e0,e1,e2,e3,e4);
	}
	
	public static <A,B,C,D,E,F> Tuple6<A,B,C,D,E,F> t( A e0 , B e1 , C e2 , D e3 , E e4 , F e5 ) {
		return t6(e0,e1,e2,e3,e4,e5);
	}


}
