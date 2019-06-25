package h2o.common.lang;

import h2o.common.util.date.DateUtil;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

public class SDate implements Comparable<SDate>, java.io.Serializable {

    private static final String DATE_FMT = "yyyy-MM-dd";

    /**
     * 日期 yyyy-MM-dd
     */
    private final String date;

    public SDate() {
        date = null;
    }

    public SDate( String date ) {
        this( toDate( date , DATE_FMT ) );
    }

    public SDate( String date , String fmt ) {
        this( toDate( date , fmt ) );
    }

    private static Date toDate(String date , String fmt ) {
        if ( date == null ) {
            return null;
        }
        return DateUtil.toDate( date , fmt );
    }


    public SDate( Date date ) {
        this.date = date == null ? null : DateUtil.toShortString( date );
    }

    public boolean isPresent() {
        return date != null;
    }


    public String get() {
        return date;
    }

    public String orElse(String other) {
        return date == null ? other : date;
    }

    public String fmt(String fmt ) {

        if ( DATE_FMT.equals( fmt ) ) {
            return date;
        }

        return DateUtil.str2Str( date , DATE_FMT , fmt );

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

        if (o == null || getClass() != o.getClass()) return false;

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
        return new ToStringBuilder(this)
                .append("date", this.orElse("<null>"))
                .toString();
    }
}
