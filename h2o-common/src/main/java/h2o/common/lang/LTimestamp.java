package h2o.common.lang;

import h2o.common.util.date.DateUtil;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class LTimestamp implements NullableValue, Comparable<LTimestamp>, java.io.Serializable {

    private static final long serialVersionUID = -1956214343180325308L;

    public static final LTimestamp NULL = new LTimestamp();

    private static final String DATE_FMT = "yyyyMMddHHmmssSSS";


    private final Long value;

    public LTimestamp() {
        this.value = null;
    }

    public LTimestamp( long timestamp ) {
        this.value = Long.valueOf(timestamp);
    }

    public LTimestamp( Long timestamp ) {
        this.value = timestamp;
    }

    public LTimestamp( SNumber timestamp ) {
        this.value = timestamp.toLong();
    }

    public LTimestamp( Instant instant ) {
        this.value = instant == null ? null : instant.toEpochMilli();
    }

    public LTimestamp( Date date ) {
        this.value = date == null ? null : date.getTime();
    }

    public LTimestamp( LTimestamp ltimestamp ) {
        this.value = ltimestamp.value;
    }

    @Override
    public boolean isPresent() {
        return this.value != null;
    }

    public Long getValue() {
        return this.value;
    }

    public String get() {

        if ( this.isPresent() ) {
            return DateUtil.toString( new Date(this.value) , DATE_FMT );
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
            return new Date(this.value);
        } else {
            throw new IllegalStateException();
        }
    }

    public Instant toInstant() {

        if ( this.isPresent() ) {
            return Instant.ofEpochMilli(this.value);
        } else {
            throw new IllegalStateException();
        }
    }

    public LocalDateTime toLocalDateTime() {
         return LocalDateTime.ofInstant( toInstant(), ZoneId.systemDefault());
    }

    public SDateTime toSDateTime() {

        if ( this.isPresent() ) {
            return new SDateTime( new Date( this.value) );
        } else {
            return new SDateTime();
        }

    }

    public SNumber toSNumber() {
        if ( this.isPresent() ) {
            return new SNumber( this.value);
        } else {
            return new SNumber();
        }
    }



    @Override
    public int compareTo( LTimestamp other ) {

        Long l = this.isPresent() ? this.value : new Long(0);
        Long r = this.isPresent() ? other.value : new Long(0);

        return l.compareTo(r);

    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || !(o instanceof LTimestamp)) return false;

        LTimestamp that = (LTimestamp) o;

        return new EqualsBuilder()
                .append(value, that.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(value)
                .toHashCode();
    }


    @Override
    public String toString() {
        return this.orElse("<null>");
    }
}
