package h2o.common.lang;

import h2o.common.util.date.DateUtil;
import h2o.common.util.lang.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

public class SDate implements NullableValue, Comparable<SDate>, java.io.Serializable {

    private static final long serialVersionUID = 9012516608507340072L;

    protected static final String DATE_FMT = "yyyy-MM-dd";


    /**
     * 日期 yyyy-MM-dd
     */
    protected final String date;

    public SDate() {
        date = null;
    }

    public SDate( String date ) {
        this( date , false );
    }

    public SDate( String dateStr , boolean direct ) {
        if ( direct ) {
            this.date = dateStr;
        } else {
            this.date = dateStr == null ? null : DateUtil.toString( toDate( dateStr , DATE_FMT ) , DATE_FMT );
        }
    }


    public SDate( int year , int month , int day ) {
        this( StringUtil.build(
                StringUtils.leftPad( Integer.toString(year) , 4 , '0') , "-" ,
                StringUtils.leftPad( Integer.toString(month) , 2 , '0') , "-" ,
                StringUtils.leftPad( Integer.toString(day) , 2 , '0') ) );
    }


    public SDate( LocalDate localDate ) {
        this.date = localDate == null ? null : StringUtil.build(
                StringUtils.leftPad( Integer.toString(localDate.getYear()) , 4 , '0') , "-" ,
                StringUtils.leftPad( Integer.toString(localDate.getMonthValue()) , 2 , '0') , "-" ,
                StringUtils.leftPad( Integer.toString(localDate.getDayOfMonth()) , 2 , '0') );
    }

    public SDate( Instant instant ) {
        this.date = instant == null ?  null : DateUtil.toString( new Date( instant.toEpochMilli() ) , DATE_FMT );
    }

    public SDate( Date d ) {
        this.date = d == null ? null : DateUtil.toString( d , DATE_FMT );
    }


    public SDate( SDate sdate ) {
        this.date = sdate.date;
    }


    protected static Date toDate( String date , String fmt ) {
        if ( date == null ) {
            return null;
        }
        try {
            return DateUtil.toDate(date, fmt);
        } catch ( Exception e ) {
            throw new IllegalArgumentException(e);
        }
    }



    public static SDate from( String date , String fmt ) {
        return new SDate( toDate( date , fmt ) );
    }




    @Override
    public boolean isPresent() {
        return date != null;
    }

    public String getValue() {
        return date;
    }

    public String get() {

        if ( this.isPresent() ) {
            return date;
        } else {
            throw new IllegalStateException();
        }

    }

    public String orElse(String other) {
        return  this.isPresent() ? date : other;
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

    public LocalDate toLocalDate() {
        return LocalDate.of( this.getYear() , this.getMonth() , this.getDay() );
    }

    public int getYear() {
        return Integer.parseInt(StringUtils.substringBefore( this.get() , "-" ) );
    }

    public int getMonth() {
        return Integer.parseInt(StringUtils.substringBetween( this.get() , "-" ) );
    }

    public int getDay() {
        return Integer.parseInt(StringUtils.substringAfterLast( this.get() , "-" ) );
    }


    @Override
    public int compareTo( SDate other ) {

        String l = this.orElse("");
        String r = other.orElse("");

        return l.compareTo(r);

    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || !(o instanceof SDate)) return false;

        SDate sDate = (SDate) o;

        return new EqualsBuilder()
                .append(date, sDate.date)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(date)
                .toHashCode();
    }

    @Override
    public String toString() {
        return this.orElse("<null>");
    }


}
