package h2o.common.lang;

import h2o.common.util.date.DateUtil;
import h2o.common.util.lang.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.time.LocalTime;
import java.util.Date;

public class STime implements Nullable, Comparable<STime>, java.io.Serializable {

    private static final long serialVersionUID = -3750549384074422469L;

    protected static final String DATE_FMT = "HH:mm:ss";

    /**
     * 时间 HH:mm:ss
     */
    private final String time;

    public STime() {
        time = null;
    }

    public STime( String time ) {
        this( time,false );
    }

    public STime( String time ,  boolean direct ) {
        if ( direct ) {
            this.time = time;
        } else {
            this.time = time == null ? null : DateUtil.toString( toDate( time , DATE_FMT ) , DATE_FMT );
        }
    }

    public static STime from(String time , String fmt ) {
        return new STime( toDate( time , fmt ) );
    }

    public STime( int hour, int minute, int second ) {
        this( StringUtil.build(StringUtils.leftPad( Integer.toString(hour) , 2 , '0') , ":" ,
                StringUtils.leftPad( Integer.toString(minute) , 2 , '0') , ":" ,
                StringUtils.leftPad( Integer.toString(second) , 2 , '0') ) );
    }

    public STime( LocalTime time ) {
        this( time.getHour() , time.getMinute() , time.getSecond() );
    }


    protected static Date toDate( String date , String fmt ) {
        return SDate.toDate( date, fmt );
    }

    public STime( Date date ) {
        this.time = date == null ? null : DateUtil.toString( date , DATE_FMT );
    }

    @Override
    public boolean isPresent() {
        return time != null;
    }

    public String getValue() {
        return time;
    }

    public String get() {

        if ( this.isPresent() ) {
            return time;
        } else {
            throw new IllegalStateException();
        }

    }

    public String orElse(String other) {
        return this.isPresent() ? time : other;
    }

    public String fmt( String fmt ) {

        if ( DATE_FMT.equals( fmt ) ) {
            return this.get();
        } else {
            return DateUtil.str2Str(this.get(), DATE_FMT, fmt);
        }

    }

    public String fmt( String fmt , String def ) {
        return this.isPresent() ? this.fmt( fmt ) : def;
    }


    public LocalTime toLocalTime() {
        return LocalTime.of( this.getHour() , this.getMinute() , this.getSecond() );
    }


    public int getHour() {
        return Integer.parseInt(StringUtils.substringBefore( this.get() , ":" ) );
    }

    public int getMinute() {
        return Integer.parseInt(StringUtils.substringBetween( this.get() , ":" ) );
    }

    public int getSecond() {
        return Integer.parseInt(StringUtils.substringAfterLast( this.get() , ":" ) );
    }




    @Override
    public int compareTo( STime other ) {

        String l = this.orElse("");
        String r = other.orElse("");

        return l.compareTo(r);

    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        STime sDate = (STime) o;

        return new EqualsBuilder()
                .append(time, sDate.time)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(time)
                .toHashCode();
    }

    @Override
    public String toString() {
        return this.orElse("<null>");
    }

}
