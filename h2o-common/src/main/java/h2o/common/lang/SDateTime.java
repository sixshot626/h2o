package h2o.common.lang;

import h2o.apache.commons.lang.StringUtils;
import h2o.common.util.date.DateUtil;
import h2o.common.util.lang.StringUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Objects;


public final class SDateTime implements OptionalValue<String>, Comparable<SDateTime>, java.io.Serializable {

    private static final long serialVersionUID = 2203956607450263142L;

    public static final SDateTime NULL = new SDateTime();

    private static final String DATE_FMT = "yyyy-MM-dd HH:mm:ss";


    /**
     * 日期时间 yyyy-MM-dd HH:mm:ss
     */
    private final String value;

    public SDateTime() {
        this.value = null;
    }

    public SDateTime(String dateTime) {
        this(dateTime, false);
    }

    public SDateTime(String dateStr, boolean direct) {
        if (direct) {
            this.value = dateStr;
        } else {
            this.value = dateStr == null ? null : DateUtil.toString(toDate(dateStr, DATE_FMT), DATE_FMT);
        }
    }


    public SDateTime(int year, int month, int day, int hour, int minute, int second) {
        this(StringUtil.build(
                StringUtils.leftPad(Integer.toString(year), 4, '0'), "-",
                StringUtils.leftPad(Integer.toString(month), 2, '0'), "-",
                StringUtils.leftPad(Integer.toString(day), 2, '0'), " ",
                StringUtils.leftPad(Integer.toString(hour), 2, '0'), ":",
                StringUtils.leftPad(Integer.toString(minute), 2, '0'), ":",
                StringUtils.leftPad(Integer.toString(second), 2, '0')));

    }

    public SDateTime(LocalDateTime localDateTime) {
        this.value = localDateTime == null ? null : StringUtil.build(
                StringUtils.leftPad(Integer.toString(localDateTime.getYear()), 4, '0'), "-",
                StringUtils.leftPad(Integer.toString(localDateTime.getMonthValue()), 2, '0'), "-",
                StringUtils.leftPad(Integer.toString(localDateTime.getDayOfMonth()), 2, '0'), " ",
                StringUtils.leftPad(Integer.toString(localDateTime.getHour()), 2, '0'), ":",
                StringUtils.leftPad(Integer.toString(localDateTime.getMinute()), 2, '0'), ":",
                StringUtils.leftPad(Integer.toString(localDateTime.getSecond()), 2, '0'));
    }

    public SDateTime(Instant instant) {
        this.value = instant == null ? null : DateUtil.toString(new Date(instant.toEpochMilli()), DATE_FMT);
    }

    public SDateTime(Date d) {
        this.value = d == null ? null : DateUtil.toString(d, DATE_FMT);
    }

    public SDateTime(SDateTime sdatetime) {
        this.value = sdatetime.value;
    }


    private static Date toDate(String date, String fmt) {
        return SDate.toDate(date, fmt);
    }


    public static SDateTime from(String dateTime, String fmt) {
        if ( StringUtils.isBlank(dateTime) ) {
            return NULL;
        }
        return new SDateTime(toDate(dateTime, fmt));
    }


    public static SDateTime now() {
        return new SDateTime( new Date() );
    }



    @Override
    public String getValue() {
        return this.value;
    }

    public SDate getDate() {

        if (this.isPresent()) {
            return SDate.from(this.value, DATE_FMT);
        } else {
            return this == NULL ? SDate.NULL : new SDate();
        }

    }


    public STime getTime() {

        if (this.isPresent()) {
            return STime.from(this.value, DATE_FMT);
        } else {
            return this == NULL ? STime.NULL : new STime();
        }

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




    public SDateTime plus(long amountToAdd, TemporalUnit unit) {
       return new SDateTime( this.toLocalDateTime().plus( amountToAdd , unit ) );
    }

    public SDateTime minus(long amountToSubtract, TemporalUnit unit) {
        return new SDateTime( this.toLocalDateTime().minus( amountToSubtract , unit ) );
    }


    public SDateTime plusYears(long years) {
        return new SDateTime( this.toLocalDateTime().plusYears( years ) );
    }


    public SDateTime plusMonths(long months) {
        return new SDateTime( this.toLocalDateTime().plusMonths( months ) );
    }


    public SDateTime plusWeeks(long weeks) {
        return new SDateTime( this.toLocalDateTime().plusWeeks( weeks ) );
    }


    public SDateTime plusDays(long days) {
        return new SDateTime( this.toLocalDateTime().plusDays( days ) );
    }


    public SDateTime plusHours(long hours) {
        return new SDateTime( this.toLocalDateTime().plusHours( hours ) );
    }


    public SDateTime plusMinutes(long minutes) {
        return new SDateTime( this.toLocalDateTime().plusMinutes( minutes ) );
    }


    public SDateTime plusSeconds(long seconds) {
        return new SDateTime( this.toLocalDateTime().plusSeconds( seconds ) );
    }



    public SDateTime minusYears(long years) {
        return new SDateTime( this.toLocalDateTime().minusYears( years ) );
    }


    public SDateTime minusMonths(long months) {
        return new SDateTime( this.toLocalDateTime().minusMonths( months ) );
    }

    public SDateTime minusWeeks(long weeks) {
        return new SDateTime( this.toLocalDateTime().minusWeeks( weeks ) );
    }


    public SDateTime minusDays(long days) {
        return new SDateTime( this.toLocalDateTime().minusDays( days ) );
    }


    public SDateTime minusHours(long hours) {
        return new SDateTime( this.toLocalDateTime().minusHours( hours ) );
    }


    public SDateTime minusMinutes(long minutes) {
        return new SDateTime( this.toLocalDateTime().minusMinutes( minutes ) );
    }


    public SDateTime minusSeconds(long seconds) {
        return new SDateTime( this.toLocalDateTime().minusSeconds( seconds ) );
    }



    public Date toDate() {
        return DateUtil.toDate(this.get(), DATE_FMT);
    }

    public LocalDateTime toLocalDateTime() {

        if (this.isPresent()) {

            SDate date = this.getDate();
            STime time = this.getTime();

            return LocalDateTime.of(date.getYear(), date.getMonth(), date.getDay(),
                    time.getHour(), time.getMinute(), time.getSecond());

        } else {
            throw new NoSuchElementException("No value present");
        }
    }


    @Override
    public int compareTo(SDateTime other) {

        String l = this.orElse("");
        String r = other.orElse("");

        return l.compareTo(r);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SDateTime sDateTime = (SDateTime) o;
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
