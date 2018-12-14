package h2o.common.util.date;

import h2o.common.Tools;
import h2o.common.exception.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;



public final class DateTime {

    private static final Logger log = LoggerFactory.getLogger( DateTime.class.getName() );


    private volatile boolean silently;
	
	

	public boolean isSilently() {
		return silently;
	}


	public void setSilently(boolean silently) {
		this.silently = silently;
	}
	
	
	public DateTime() {		
		this.silently = true;
	}

	public DateTime(boolean isSilently) {		
		this.silently = isSilently;
	}




	private static final String fmt = "yyyy-MM-dd HH:mm:ss";
	public String toString(Date d) {
		return toString( d , fmt );
	}

	private static final String shortFmt = "yyyy-MM-dd";
	public String toShortString(Date d) {
		return toString( d , shortFmt );
	}

	private static final String longFmt = "yyyy-MM-dd HH:mm:ss.SSS";
	public String toLongString(Date d) {
		return toString( d , longFmt );
	}



    private static final String timeFmt = "HH:mm:ss";
    public String toTimeString(Date d) {
        return toString( d , timeFmt );
    }



    public String toString(Date d, String fmt) {
		return new SimpleDateFormat(fmt).format(d);
	}
	
	public Date toDate( String sd ) {
		return toDate(sd,"yyyy-MM-dd");			
	}
	
	
	public Date toDate( String sd , String fmt ) {
		try {
			return new SimpleDateFormat(fmt).parse(sd);
		} catch (ParseException e) {			
			log.debug("toDate" , e);
			if( !this.silently ) {
				throw ExceptionUtil.toRuntimeException(e);
			}
		}
		
		return null;
	}
	
	public String str2Str( String sd , String sfmt , String tfmt ) {
		return toString( toDate(sd , sfmt) , tfmt );
	}
	

	public int getDaysOfMonth(String year, String month) {
		Calendar cal = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, 1);
		int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return days;
	}

	public int getDaysBetween(Date date_start, Date date_end) {
        try {

            Calendar d1 = Calendar.getInstance();
            Calendar d2 = Calendar.getInstance();
            d1.setTime(date_start);
            d2.setTime(date_end);
            if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
                Calendar swap = d1;
                d1 = d2;
                d2 = swap;
            }
            int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
            int y2 = d2.get(Calendar.YEAR);
            if (d1.get(Calendar.YEAR) != y2) {
                d1 = (Calendar) d1.clone();
                do {
                    days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                    d1.add(Calendar.YEAR, 1);
                } while (d1.get(Calendar.YEAR) != y2);
            }
            return days;
        } catch (Exception e) {
            log.debug("getDaysBetween", e);
            throw ExceptionUtil.toRuntimeException(e);
        }
    }



    public int getMonthsBetween( Date date_start, Date date_end ) {

        try {

            Calendar d1 = Calendar.getInstance();
            Calendar d2 = Calendar.getInstance();
            d1.setTime(date_start);
            d2.setTime(date_end);
            if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
                Calendar swap = d1;
                d1 = d2;
                d2 = swap;
            }

            int ms = d2.get(Calendar.MONTH) - d1.get(Calendar.MONTH);

            int y2 = d2.get(Calendar.YEAR);
            int y1 = d1.get(Calendar.YEAR);

            ms += ( y2 - y1 ) * 12;

            if( d2.get( Calendar.DAY_OF_MONTH ) < d1.get( Calendar.DAY_OF_MONTH )
                    /*&& d2.get( Calendar.DAY_OF_MONTH ) != d2.getActualMaximum( Calendar.DATE ) */ ) {

                ms -= 1;

            }

            return ms;

        } catch (Exception e) {
            log.debug("getMonthsBetween", e);
            throw ExceptionUtil.toRuntimeException(e);
        }
    }



    public int getActualMaximum( Date date ) {
        
        Calendar d = Calendar.getInstance();
        d.setTime( date );

        return d.getActualMaximum( Calendar.DATE );
        
    }



	
	public Date getAfterDay( Date date, int count ) {
		return getAfterDate( date , Calendar.DATE , count );
	}

	public Date getAfterDate( Date date, int type , int count ) {
		try {
		
			Calendar c = Calendar.getInstance();
			c.setTime( date );
			c.add( type, count );
			
			return c.getTime();
			
		} catch ( Exception e ) {
			log.debug("getAfterDate", e);
			
			if( this.silently ) {
				return null;
			} else {
				throw ExceptionUtil.toRuntimeException(e);
			}
		}
	}


}
