package h2o.common.lang;

import h2o.common.util.date.DateUtil;
import h2o.common.util.lang.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class SDateTime implements Comparable<SDateTime>, java.io.Serializable {

    private static final long serialVersionUID = 3242879174207238197L;

    protected static final String DATE_FMT = "yyyy-MM-ddTHH:mm:ss";
    
    /**
     * 日期时间 yyyy-MM-dd HH:mm:ss
     */
    private final String dateTime;

    public SDateTime() {
        dateTime = null;
    }

    public SDateTime( String dateTime ) {
        this( dateTime , false );
    }

    public SDateTime( String dateTime , boolean direct ) {
        if ( direct ) {
            this.dateTime = dateTime;
        } else {
            this.dateTime = dateTime == null ? null : DateUtil.toString( toDate(dateTime, DATE_FMT ) , DATE_FMT );
        }
    }

    public static SDateTime from( String dateTime, String fmt ) {
        return new SDateTime( toDate(dateTime, fmt ) );
    }


    public SDateTime( int year , int month , int day , int hour, int minute, int second ) {

        this( StringUtil.build( StringUtils.leftPad( Integer.toString(year) , 4 , '0') , "-" ,
                StringUtils.leftPad( Integer.toString(month) , 2 , '0') , "-" ,
                StringUtils.leftPad( Integer.toString(day) , 2 , '0')  , "T" ,
                StringUtils.leftPad( Integer.toString(hour) , 2 , '0') , ":" ,
                StringUtils.leftPad( Integer.toString(minute) , 2 , '0') , ":" ,
                StringUtils.leftPad( Integer.toString(second) , 2 , '0') ) );

    }

    public SDateTime( LocalDateTime dateTime ) {

        this( dateTime.getYear() , dateTime.getMonthValue() , dateTime.getDayOfMonth() ,
                dateTime.getHour(), dateTime.getMinute() , dateTime.getSecond());

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



    public Date toDate() {
        return DateUtil.toDate( this.get() , DATE_FMT );
    }

    public LocalDateTime toLocalDateTime() {

        SDate date = this.getDate();
        STime time = this.getTime();

        return LocalDateTime.of( date.getYear() , date.getMonth() , date.getDay() ,
                time.getHour() , time.getMinute() , time.getSecond() );
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
        return this.orElse("<null>");
    }
}
