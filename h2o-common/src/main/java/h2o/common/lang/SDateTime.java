package h2o.common.lang;

import h2o.common.util.date.DateUtil;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

public final class SDateTime implements Comparable<SDateTime>, java.io.Serializable {

    private static final String DATE_FMT = "yyyy-MM-dd HH:mm:ss";

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

    private static Date toDate( String date , String fmt ) {
        if ( date == null ) {
            return null;
        }
        try {
            return DateUtil.toDate(date, fmt);
        } catch ( Exception e ) {
            throw new IllegalArgumentException();
        }
    }


    public SDateTime(Date dateTime) {
        this.dateTime = dateTime == null ? null : DateUtil.toString(dateTime);
    }

    public boolean isPresent() {
        return dateTime != null;
    }


    public String getValue() {
        return dateTime;
    }


    public String get() {
        if ( this.dateTime == null ) {
            throw new NullPointerException();
        }
        return dateTime;
    }

    public SDate getDate() {
        if ( this.dateTime == null ) {
            return new SDate();
        }
        return SDate.from( this.dateTime , DATE_FMT );
    }


    public STime getTime() {
        if ( this.dateTime == null ) {
            return new STime();
        }
        return STime.from( this.dateTime , DATE_FMT );
    }


    public String orElse(String other) {
        return dateTime == null ? other : dateTime;
    }

    public String fmt( String fmt ) {

        if ( DATE_FMT.equals( fmt ) ) {
            return this.get();
        }

        return DateUtil.str2Str( this.get() , DATE_FMT , fmt );

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
