package h2o.common.collections;

import h2o.common.exception.ExceptionUtil;


@SuppressWarnings("unchecked")
public class Args implements java.io.Serializable {	

	private static final long serialVersionUID = -1159068677332269042L;

	public volatile int c;
	
	public volatile Object a0;
	
	public volatile Object a1;
	
	public volatile Object a2;
	
	public volatile Object a3;
	
	public volatile Object a4;
	
	public volatile Object a5;
	
	public volatile Object a6;
	
	public volatile Object a7;
	
	public volatile Object a8;
	
	public volatile Object a9;
	
	public volatile Object a10;
	
	public Args() {}

	public Args( int c , Object... args ) {

		this.c = c;

		try {
			int len = args.length;
			if ( len > 0 ) this.a0 = args[0];
			if ( len > 1 ) this.a1 = args[1];
			if ( len > 2 ) this.a2 = args[2];
			if ( len > 3 ) this.a3 = args[3];
			if ( len > 4 ) this.a4 = args[4];
			if ( len > 5 ) this.a5 = args[5];
			if ( len > 6 ) this.a6 = args[6];
			if ( len > 7 ) this.a7 = args[7];
			if ( len > 8 ) this.a8 = args[8];
			if ( len > 9 ) this.a9 = args[9];
			if ( len > 10 ) this.a10 = args[10];

		} catch( Exception e ) {
			throw ExceptionUtil.toRuntimeException(e);
		}
	}

	public int getC() {
		return c;
	}

	public Args setC(int c) {
		this.c = c;
		return this;
	}

	public <T> T getA0() {
		return (T)a0;
	}

	public Args setA0(Object a0) {
		this.a0 = a0;
		return this;
	}

	public <T> T getA1() {
		return (T)a1;
	}

	public Args setA1(Object a1) {
		this.a1 = a1;
		return this;
	}

	public <T> T getA2() {
		return (T)a2;
	}

	public Args setA2(Object a2) {
		this.a2 = a2;
		return this;
	}

	public <T> T getA3() {
		return (T)a3;
	}

	public Args setA3(Object a3) {
		this.a3 = a3;
		return this;
	}

	public <T> T getA4() {
		return (T)a4;
	}

	public Args setA4(Object a4) {
		this.a4 = a4;
		return this;
	}

	public <T> T getA5() {
		return (T)a5;
	}

	public Args setA5(Object a5) {
		this.a5 = a5;
		return this;
	}

	public <T> T getA6() {
		return (T)a6;
	}

	public Args setA6(Object a6) {
		this.a6 = a6;
		return this;
	}

	public <T> T getA7() {
		return (T)a7;
	}

	public Args setA7(Object a7) {
		this.a7 = a7;
		return this;
	}

	public <T> T getA8() {
		return (T)a8;
	}

	public Args setA8(Object a8) {
		this.a8 = a8;
		return this;
	}

	public <T> T getA9() {
		return (T)a9;
	}

	public Args setA9(Object a9) {
		this.a9 = a9;
		return this;
	}

	public <T> T getA10() {
		return (T)a10;
	}

	public Args setA10(Object a10) {
		this.a10 = a10;
		return this;
	}

	public Object[] getArgs() {
		return new Object[] { a0,a1,a2,a3,a4,a5,a6,a7,a8,a9,a10 };
	}

	@Override
	public String toString() {
		return "Args [c=" + c + ", a0=" + a0 + ", a1=" + a1 + ", a2=" + a2 + ", a3=" + a3 + ", a4=" + a4 + ", a5=" + a5 + ", a6=" + a6 + ", a7=" + a7 + ", a8=" + a8 + ", a9=" + a9 + ", a10=" + a10 + "]";
	}


}
