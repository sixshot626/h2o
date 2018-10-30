package h2o.common.util.date;

import java.util.Date;



public abstract class DateUtil {

	private DateUtil() {};
	
	private final static DateTime dateTime = new DateTime(false);
	

	public static String toString(Date d) {
		return dateTime.toString(d);
	}

	public static String toShortString(Date d) {
		return dateTime.toShortString(d);
	}

	public static String toLongString(Date d) {
		return dateTime.toLongString(d);
	}

    public static String toTimeString(Date d) {
        return dateTime.toTimeString(d);
    }

	public static String toString(Date d, String fmt) {
		return dateTime.toString(d,fmt);
	}
	
	
	public static Date toDate( String sd ) {
		return dateTime.toDate( sd );
	}
	
	public static Date toDate( String sd , String fmt ) {
		return dateTime.toDate( sd , fmt );
	}	
	
	public static String str2Str( String sd , String sfmt , String tfmt ) {
		return dateTime.str2Str(sd, sfmt, tfmt);
	}
	
	

	public static int getDaysOfMonth( String year, String month ) {
		return dateTime.getDaysOfMonth( year,  month);
	}

	public static int getDaysBetween(Date date_start, Date date_end ) {
		return dateTime.getDaysBetween( date_start,  date_end );
	}

    public static int getMonthsBetween(Date date_start, Date date_end ) {
        return dateTime.getMonthsBetween( date_start,  date_end );
    }
	
	
	public static Date getAfterDay( Date date, int countdate ) {
		return dateTime.getAfterDay( date , countdate);
	}

	public static Date getAfterDay( Date date, int type , int countdate) {
		return dateTime.getAfterDay(  date,  type ,  countdate);
	}

}
