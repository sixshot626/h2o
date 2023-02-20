package h2o.common.lang;

import h2o.common.util.date.DateUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Objects;

public final class STimestamp implements OptionalValue<String>, Comparable<STimestamp>, java.io.Serializable {

    private static final long serialVersionUID = -3720779065762557947L;

    public static final STimestamp NULL = new STimestamp();

    private static final String DATE_FMT = "yyyyMMddHHmmssSSS";


    private final String value;

    public STimestamp() {
        this.value = null;
    }

    public STimestamp(String dateTime) {
        this(dateTime, false);
    }

    public STimestamp(String dateStr, boolean direct) {
        if (direct) {
            this.value = dateStr;
        } else {
            this.value = dateStr == null ? null : DateUtil.toString(toDate(dateStr, DATE_FMT), DATE_FMT);
        }
    }

    public STimestamp(Instant instant) {
        this.value = instant == null ? null : DateUtil.toString(new Date(instant.toEpochMilli()), DATE_FMT);
    }

    public STimestamp(Date d) {
        this.value = d == null ? null : DateUtil.toString(d, DATE_FMT);
    }

    public STimestamp(STimestamp sdatetime) {
        this.value = sdatetime.value;
    }


    private static Date toDate(String date, String fmt) {
        return SDate.toDate(date, fmt);
    }


    public static STimestamp from(String dateTime, String fmt) {
        return new STimestamp(toDate(dateTime, fmt));
    }


    public static STimestamp now() {
        return new STimestamp( new Date() );
    }


    @Override
    public String getValue() {
        return this.value;
    }


    public String fmt(String fmt) {

        if (DATE_FMT.equals(fmt)) {
            return this.get();
        } else {
            return DateUtil.str2Str(this.get(), DATE_FMT, fmt);
        }

    }

    public String fmt(String fmt, String def) {
        return this.isPresent() ? this.fmt(fmt) : def;
    }




    public STimestamp plusSeconds(long secondsToAdd) {
       return new STimestamp( this.toInstant().plusSeconds( secondsToAdd ) );
    }

    public STimestamp plusMillis(long millisToAdd) {
        return new STimestamp( this.toInstant().plusMillis( millisToAdd ) );
    }


    public STimestamp minusSeconds(long secondsToSubtract) {
        return new STimestamp( this.toInstant().minusSeconds( secondsToSubtract ) );
    }

    public STimestamp minusMillis(long millisToSubtract) {
        return new STimestamp( this.toInstant().minusMillis( millisToSubtract ) );
    }




    public SDateTime toSDateTime() {

        if (this.isPresent()) {
            return SDateTime.from(this.value, DATE_FMT);
        } else {
            return this == NULL ? SDateTime.NULL : new SDateTime();
        }

    }

    public LTime toLTime() {

        if (this.isPresent()) {
            return LTime.from(this.value, DATE_FMT);
        } else {
            return this == NULL ? LTime.NULL : new LTime();
        }

    }

    public Date toDate() {
        return DateUtil.toDate(this.get(), DATE_FMT);
    }


    public Instant toInstant() {
        return Instant.ofEpochMilli(this.toDate().getTime());
    }

    public LocalDateTime toLocalDateTime() {

        if (this.isPresent()) {

            SDateTime dt = this.toSDateTime();

            SDate date = dt.getDate();
            STime time = dt.getTime();

            return LocalDateTime.of(date.getYear(), date.getMonth(), date.getDay(),
                    time.getHour(), time.getMinute(), time.getSecond());

        } else {
            throw new NoSuchElementException("No value present");
        }
    }


    @Override
    public int compareTo(STimestamp other) {

        String l = this.orElse("");
        String r = other.orElse("");

        return l.compareTo(r);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        STimestamp sDateTime = (STimestamp) o;
        return Objects.equals(value, sDateTime.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return this.orElse("<null>");
    }


}
