package h2o.common.lang;

import h2o.common.util.date.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

public class SDateTime implements Comparable<SDateTime>, java.io.Serializable {

    protected static final String DATE_FMT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期时间 yyyy-MM-dd HH:mm:ss
     */
    private final String dateTime;

    public SDateTime() {
        dateTime = null;
    }

    public SDateTime(String dateTime) {
        this( toDate(dateTime, DATE_FMT ) );
    }

    public static SDateTime from( String dateTime, String fmt ) {
        return new SDateTime( toDate(dateTime, fmt ) );
    }


    public SDateTime( int year , int month , int day , int hour, int minute, int second ) {

        this( StringUtils.leftPad( Integer.toString(year) , 4 , '0') + "-" +
                StringUtils.leftPad( Integer.toString(month) , 2 , '0') + "-" +
                StringUtils.leftPad( Integer.toString(day) , 2 , '0')  + " " +
                StringUtils.leftPad( Integer.toString(hour) , 2 , '0') + ":" +
                StringUtils.leftPad( Integer.toString(minute) , 2 , '0') + ":" +
                StringUtils.leftPad( Integer.toString(second) , 2 , '0')  );

    }


    protected static Date toDate( String date , String fmt ) {
       return SDate.toDate( date, fmt );
    }


    public SDateTime(Date dateTime) {
        this.dateTime = dateTime == null ? null : DateUtil.toString( dateTime , DATE_FMT );
    }

    public boolean isPresent() {
        return dateTime != null;
    }


    public String getValue() {
        return dateTime;
    }


    public String get() {

        if ( this.isPresent() ) {
            return dateTime;
        }

        throw new IllegalStateException();

    }

    public SDate getDate() {

        if ( this.isPresent() ) {
            return SDate.from( this.dateTime , DATE_FMT );
        }

        return new SDate();

    }


    public STime getTime() {

        if ( this.isPresent() ) {
            return STime.from( this.dateTime , DATE_FMT );
        }

        return new STime();

    }


    public String orElse(String other) {
        return this.isPresent() ?  dateTime : other;
    }

    public String fmt( String fmt ) {

        if ( DATE_FMT.equals( fmt ) ) {
            return this.get();
        }

        return DateUtil.str2Str( this.get() , DATE_FMT , fmt );

    }

    public String fmt( String fmt , String def ) {
        return this.isPresent() ? this.fmt( fmt ) : def;
    }



    @Override
    public int compareTo( SDateTime other ) {

        String l = this.orElse("");
        String r = other.orElse("");

        return l.compareTo(r);

    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SDateTime sDate = (SDateTime) o;

        return new EqualsBuilder()
                .append(dateTime, sDate.dateTime)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(dateTime)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("dateTime", this.orElse("<null>"))
                .toString();
    }
}
