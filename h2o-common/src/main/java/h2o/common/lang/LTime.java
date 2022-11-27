package h2o.common.lang;

import h2o.common.util.date.DateUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;

public final class LTime implements OptionalValue<Long>, Comparable<LTime>, java.io.Serializable {

    private static final long serialVersionUID = 1239958588661309274L;

    public static final LTime NULL = new LTime();

    private static final String DATE_FMT = "yyyyMMddHHmmssSSS";


    private final Long value;

    public LTime() {
        this.value = null;
    }

    public LTime(long timestamp) {
        this.value = Long.valueOf(timestamp);
    }

    public LTime(Long timestamp) {
        this.value = timestamp;
    }

    public LTime(SNumber timestamp) {
        this.value = timestamp.toLong();
    }

    public LTime(Instant instant) {
        this.value = instant == null ? null : instant.toEpochMilli();
    }

    public LTime(Date date) {
        this.value = date == null ? null : date.getTime();
    }

    public LTime(LTime ltimestamp) {
        this.value = ltimestamp.value;
    }


    public static LTime now() {
        return new LTime( System.currentTimeMillis() );
    }


    @Override
    public Long getValue() {
        return this.value;
    }


    public String getTimestampString() {
        return DateUtil.toString(new Date(this.get()), DATE_FMT);
    }

    public String orElseString(String other) {
        return this.isPresent() ? this.getTimestampString() : other;
    }

    public <X extends Throwable> String orElseStringThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (this.isPresent()) {
            return this.getTimestampString();
        } else {
            throw exceptionSupplier.get();
        }
    }


    public String fmt(String fmt) {

        if (DATE_FMT.equals(fmt)) {
            return this.getTimestampString();
        } else {
            return DateUtil.str2Str(this.getTimestampString(), DATE_FMT, fmt);
        }

    }

    public String fmt(String fmt, String def) {
        return this.isPresent() ? this.fmt(fmt) : def;
    }


    public Date toDate() {

        if (this.isPresent()) {
            return new Date(this.value);
        } else {
            throw new NoSuchElementException("No value present");
        }
    }

    public Instant toInstant() {

        if (this.isPresent()) {
            return Instant.ofEpochMilli(this.value);
        } else {
            throw new NoSuchElementException("No value present");
        }
    }

    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.ofInstant(toInstant(), ZoneId.systemDefault());
    }

    public SDateTime toSDateTime() {

        if (this.isPresent()) {
            return new SDateTime(new Date(this.value));
        } else {
            return new SDateTime();
        }

    }

    public SNumber toSNumber() {
        if (this.isPresent()) {
            return new SNumber(this.value);
        } else {
            return new SNumber();
        }
    }


    @Override
    public int compareTo(LTime other) {

        Long l = this.isPresent() ? this.value : new Long(0);
        Long r = this.isPresent() ? other.value : new Long(0);

        return l.compareTo(r);

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LTime lTime = (LTime) o;
        return Objects.equals(value, lTime.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return this.orElseString("<null>");
    }
}
