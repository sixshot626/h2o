package h2o.common.lang;

import h2o.common.util.date.DateUtil;
import h2o.common.util.lang.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

public class STime implements NullableValue, Comparable<STime>, java.io.Serializable {

    private static final long serialVersionUID = -3750549384074422469L;

    public static final STime NULL = new STime();

    protected static final String DATE_FMT = "HH:mm:ss";

    /**
     * 时间 HH:mm:ss
     */
    protected final String time;

    public STime() {
        time = null;
    }

    public STime( String time ) {
        this( time,false );
    }

    public STime( String timeStr ,  boolean direct ) {
        if ( direct ) {
            this.time = timeStr;
        } else {
            this.time = timeStr == null ? null : DateUtil.toString( toDate( timeStr , DATE_FMT ) , DATE_FMT );
        }
    }

    public STime( int hour, int minute, int second ) {
        this( StringUtil.build(StringUtils.leftPad( Integer.toString(hour) , 2 , '0') , ":" ,
                StringUtils.leftPad( Integer.toString(minute) , 2 , '0') , ":" ,
                StringUtils.leftPad( Integer.toString(second) , 2 , '0') ) );
    }

    public STime( LocalTime localTime ) {
        this.time = localTime == null ? null : StringUtil.build(
                StringUtils.leftPad( Integer.toString(localTime.getHour()) , 2 , '0') , ":" ,
                StringUtils.leftPad( Integer.toString(localTime.getMinute()) , 2 , '0') , ":" ,
                StringUtils.leftPad( Integer.toString(localTime.getSecond()) , 2 , '0') );
    }

    public STime( Instant instant ) {
        this.time = instant == null ?  null : DateUtil.toString( new Date( instant.toEpochMilli() ) , DATE_FMT );
    }

    public STime( Date d ) {
        this.time = d == null ? null : DateUtil.toString( d , DATE_FMT );
    }

    public STime( STime stime ) {
        this.time = stime.time;
    }


    protected static Date toDate( String date , String fmt ) {
        return SDate.toDate( date, fmt );
    }

    public static STime from(String time , String fmt ) {
        return new STime( toDate( time , fmt ) );
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

        if (o == null || !(o instanceof STime)) return false;

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
