package h2o.common.lang;

import h2o.common.util.date.DateUtil;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

public class LTimestamp implements Comparable<LTimestamp>, java.io.Serializable {

    protected static final String DATE_FMT = "yyyyMMddHHmmssSSS";

    private final Long timestamp;

    public LTimestamp() {
        this.timestamp = null;
    }

    public LTimestamp( Long timestamp) {
        this.timestamp = timestamp;
    }

    public LTimestamp( Date date ) {
        this.timestamp = date == null ? null : date.getTime();
    }

    public boolean isPresent() {
        return timestamp != null;
    }

    public Long getValue() {
        return timestamp;
    }

    public String get() {

        if ( this.isPresent() ) {
            return DateUtil.toString( new Date(this.timestamp) , DATE_FMT );
        }

        throw new IllegalStateException();

    }

    public SDateTime getDateTime() {

        if ( this.isPresent() ) {
            return new SDateTime( new Date( this.timestamp ) );
        }

        return new SDateTime();

    }

    public String orElse(String other) {
        return this.isPresent() ? this.get() : other;
    }

    public String fmt( String fmt ) {

        if ( DATE_FMT.equals( fmt ) ) {
            return this.get();
        }

        return DateUtil.str2Str( this.get() , DATE_FMT , fmt );

    }


    @Override
    public int compareTo( LTimestamp other ) {

        Long l = this.isPresent() ? this.timestamp : new Long(0);
        Long r = this.isPresent() ? other.timestamp : new Long(0);

        return l.compareTo(r);

    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        LTimestamp that = (LTimestamp) o;

        return new EqualsBuilder()
                .append(timestamp, that.timestamp)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(timestamp)
                .toHashCode();
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("timestamp", timestamp)
                .toString();
    }
}
