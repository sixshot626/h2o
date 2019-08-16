package h2o.common.lang;

import h2o.common.util.date.DateUtil;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

public class STime implements Comparable<STime>, java.io.Serializable {

    protected static final String DATE_FMT = "HH:mm:ss";

    /**
     * 时间 HH:mm:ss
     */
    private final String time;

    public STime() {
        time = null;
    }

    public STime(String time ) {
        this( toDate( time , DATE_FMT ) );
    }

    public static STime from(String time , String fmt ) {
        return new STime( toDate( time , fmt ) );
    }

    protected static Date toDate( String date , String fmt ) {
        return SDate.toDate( date, fmt );
    }

    public STime( Date date ) {
        this.time = date == null ? null : DateUtil.toTimeString( date );
    }

    public boolean isPresent() {
        return time != null;
    }

    public String getValue() {
        return time;
    }

    public String get() {
        if ( this.time == null ) {
            throw new NullPointerException();
        }
        return time;
    }

    public String orElse(String other) {
        return time == null ? other : time;
    }

    public String fmt( String fmt ) {

        if ( DATE_FMT.equals( fmt ) ) {
            return this.get();
        }

        return DateUtil.str2Str( this.get() , DATE_FMT , fmt );

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
        return new ToStringBuilder(this)
                .append("time", this.orElse("<null>"))
                .toString();
    }
}
