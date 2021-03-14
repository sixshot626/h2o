package h2o.common.lang;

import h2o.common.util.date.DateUtil;
import h2o.common.util.lang.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

public class SDateTime implements NullableValue, Comparable<SDateTime>, java.io.Serializable {

    private static final long serialVersionUID = 3242879174207238197L;

    public static final SDateTime NULL = new SDateTime();

    protected static final String DATE_FMT = "yyyy-MM-dd'T'HH:mm:ss";
    
    /**
     * 日期时间 yyyy-MM-dd HH:mm:ss
     */
    protected final String dateTime;

    public SDateTime() {
        dateTime = null;
    }

    public SDateTime( String dateTime ) {
        this( dateTime , false );
    }

    public SDateTime( String dateStr , boolean direct ) {
        if ( direct ) {
            this.dateTime = dateStr;
        } else {
            this.dateTime = dateStr == null ? null : DateUtil.toString( toDate(dateStr, DATE_FMT ) , DATE_FMT );
        }
    }


    public SDateTime( int year , int month , int day , int hour, int minute, int second ) {
        this( StringUtil.build(
                StringUtils.leftPad( Integer.toString(year) , 4 , '0') , "-" ,
                StringUtils.leftPad( Integer.toString(month) , 2 , '0') , "-" ,
                StringUtils.leftPad( Integer.toString(day) , 2 , '0')  , "T" ,
                StringUtils.leftPad( Integer.toString(hour) , 2 , '0') , ":" ,
                StringUtils.leftPad( Integer.toString(minute) , 2 , '0') , ":" ,
                StringUtils.leftPad( Integer.toString(second) , 2 , '0') ) );

    }

    public SDateTime( LocalDateTime localDateTime ) {
        this.dateTime = localDateTime == null ? null : StringUtil.build(
                StringUtils.leftPad( Integer.toString(localDateTime.getYear()) , 4 , '0') , "-" ,
                StringUtils.leftPad( Integer.toString(localDateTime.getMonthValue()) , 2 , '0') , "-" ,
                StringUtils.leftPad( Integer.toString(localDateTime.getDayOfMonth()) , 2 , '0')  , "T" ,
                StringUtils.leftPad( Integer.toString(localDateTime.getHour()) , 2 , '0') , ":" ,
                StringUtils.leftPad( Integer.toString(localDateTime.getMinute()) , 2 , '0') , ":" ,
                StringUtils.leftPad( Integer.toString(localDateTime.getSecond()) , 2 , '0') );
    }

    public SDateTime( Instant instant ) {
        this.dateTime = instant == null ?  null : DateUtil.toString( new Date( instant.toEpochMilli() ) , DATE_FMT );
    }

    public SDateTime( Date d ) {
        this.dateTime = d == null ? null : DateUtil.toString( d , DATE_FMT );
    }

    public SDateTime( SDateTime sdatetime ) {
        this.dateTime = sdatetime.dateTime;
    }


    protected static Date toDate( String date , String fmt ) {
        return SDate.toDate( date, fmt );
    }



    public static SDateTime from( String dateTime, String fmt ) {
        return new SDateTime( toDate(dateTime, fmt ) );
    }


    @Override
    public boolean isPresent() {
        return dateTime != null;
    }


    public String getValue() {
        return dateTime;
    }


    public String get() {

        if ( this.isPresent() ) {
            return dateTime;
        } else {
            throw new IllegalStateException();
        }

    }

    public SDate getDate() {

        if ( this.isPresent() ) {
            return SDate.from( this.dateTime , DATE_FMT );
        } else {
            return new SDate();
        }

    }


    public STime getTime() {

        if ( this.isPresent() ) {
            return STime.from( this.dateTime , DATE_FMT );
        } else {
            return new STime();
        }

    }


    public String orElse(String other) {
        return this.isPresent() ?  dateTime : other;
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



    public Date toDate() {
        return DateUtil.toDate( this.get() , DATE_FMT );
    }

    public LocalDateTime toLocalDateTime() {

        if ( this.isPresent() ) {

            SDate date = this.getDate();
            STime time = this.getTime();

            return LocalDateTime.of(date.getYear(), date.getMonth(), date.getDay(),
                    time.getHour(), time.getMinute(), time.getSecond());

        } else {
            throw new IllegalStateException();
        }
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

        if (o == null || !(o instanceof SDateTime)) return false;

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
        return this.orElse("<null>");
    }


}
