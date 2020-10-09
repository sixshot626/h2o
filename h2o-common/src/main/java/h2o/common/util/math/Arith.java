package h2o.common.util.math;

import h2o.common.thirdparty.spring.util.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Arith {

	// 默认除法运算精度

	private static final int DEF_DIV_SCALE = 16;

	// 这个类不能实例化

	private Arith() {

	}

	/**
	 * 
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 *            被加数
	 * 
	 * @param v2
	 *            加数
	 * 
	 * @return 两个参数的和
	 * 
	 */

	public static <T> T add( T v1, T v2) {

		BigDecimal b1 = toBigDecimal(v1);

		BigDecimal b2 = toBigDecimal(v2);

		return toOtherType(b1.add(b2),v1);

	}

	/**
	 * 
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * 
	 * @param v2
	 *            减数
	 * 
	 * @return 两个参数的差
	 * 
	 */

	public static <T> T sub( T v1, T v2) {

		BigDecimal b1 = toBigDecimal(v1);

		BigDecimal b2 = toBigDecimal(v2);

		return toOtherType( b1.subtract(b2) , v1 );

	}

	/**
	 * 
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * 
	 * @param v2
	 *            乘数
	 * 
	 * @return 两个参数的积
	 * 
	 */

	public static <T> T mul(T v1, T v2) {

		BigDecimal b1 = toBigDecimal(v1);
		BigDecimal b2 = toBigDecimal(v2);

		return toOtherType( b1.multiply(b2) , v1 );

	}

	/**
	 * 
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
	 * 
	 * 小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * 
	 * @param v2
	 *            除数
	 * 
	 * @return 两个参数的商
	 * 
	 */

	public static <T> T div( T v1, T v2 ) {

		return div(v1, v2, DEF_DIV_SCALE);

	}

	/**
	 * 
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
	 * 
	 * 定精度，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * 
	 * @param v2
	 *            除数
	 * 
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * 
	 * @return 两个参数的商
	 * 
	 */

	public static <T> T div( T v1, T v2, int scale) {

        return div(v1,v2,scale,RoundingMode.HALF_UP);

    }

    public static <T> T div( T v1, T v2, int scale , RoundingMode roundingMode ) {

        if (scale < 0) {

            throw new IllegalArgumentException(

                    "The scale must be a positive integer or zero");

        }

        BigDecimal b1 = toBigDecimal(v1);

        BigDecimal b2 = toBigDecimal(v2);

        return toOtherType( b1.divide(b2, scale, roundingMode) , v1 );

    }

	/**
	 * 
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * 
	 * @param scale
	 *            小数点后保留几位
	 * 
	 * @return 四舍五入后的结果
	 * 
	 */

    public static <T> T round(T v, int scale ) {

        return round(v,scale,RoundingMode.HALF_UP);

    }

	public static <T> T round(T v, int scale , RoundingMode roundingMode) {

		if (scale < 0) {

			throw new IllegalArgumentException(

			"The scale must be a positive integer or zero");

		}

		BigDecimal b = toBigDecimal(v);

		BigDecimal one = new BigDecimal("1");

		return toOtherType(b.divide( one , scale , roundingMode ) , v );

	}


	
	
	public static <T> int compareTo( T v1, T v2 ) {

		BigDecimal b1 = toBigDecimal(v1);

		BigDecimal b2 = toBigDecimal(v2);

		return b1.compareTo(b2);

	}
	
	public static BigDecimal toBigDecimal( Object o ) {
		if( o == null) {
			throw new NullPointerException();
		}
		if( o instanceof BigDecimal ) {
			return (BigDecimal)o;
		} 
		
		return (new BigDecimal(o.toString()));
		
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T toOtherType( BigDecimal b , T t) {
		
		
		if( t instanceof BigDecimal ) {
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
	


};
