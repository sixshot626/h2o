package h2o.common.lang;

import h2o.common.util.date.DateUtil;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class LTimestamp implements NullableValue, Comparable<LTimestamp>, java.io.Serializable {

    private static final long serialVersionUID = -6995945806906201103L;

    protected static final String DATE_FMT = "yyyyMMddHHmmssSSS";

    private final Long timestamp;

    public LTimestamp() {
        this.timestamp = null;
    }

    public LTimestamp( long timestamp ) {
        this.timestamp = Long.valueOf(timestamp);
    }

    public LTimestamp( Long timestamp ) {
        this.timestamp = timestamp;
    }

    public LTimestamp( SNumber timestamp ) {
        this.timestamp = timestamp.toLong();
    }

    public LTimestamp( Instant instant ) {
        this.timestamp = instant == null ? null : instant.toEpochMilli();
    }

    public LTimestamp( Date date ) {
        this.timestamp = date == null ? null : date.getTime();
    }

    public LTimestamp( LTimestamp ltimestamp ) {
        this.timestamp = ltimestamp.timestamp;
    }

    @Override
    public boolean isPresent() {
        return timestamp != null;
    }

    public Long getValue() {
        return timestamp;
    }

    public String get() {

        if ( this.isPresent() ) {
            return DateUtil.toString( new Date(this.timestamp) , DATE_FMT );
        } else {
            throw new IllegalStateException();
        }

    }


    public String orElse(String other) {
        return this.isPresent() ? this.get() : other;
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

        if ( this.isPresent() ) {
            return new Date(this.timestamp);
        } else {
            throw new IllegalStateException();
        }
    }

    public Instant toInstant() {

        if ( this.isPresent() ) {
            return Instant.ofEpochMilli(this.timestamp);
        } else {
            throw new IllegalStateException();
        }
    }

    public LocalDateTime toLocalDateTime() {
         return LocalDateTime.ofInstant( toInstant(), ZoneId.systemDefault());
    }

    public SDateTime toSDateTime() {

        if ( this.isPresent() ) {
            return new SDateTime( new Date( this.timestamp ) );
        } else {
            return new SDateTime();
        }

    }

    public SNumber toSNumber() {
        if ( this.isPresent() ) {
            return new SNumber( this.timestamp );
        } else {
            return new SNumber();
        }
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
        return this.orElse("<null>");
    }
}
