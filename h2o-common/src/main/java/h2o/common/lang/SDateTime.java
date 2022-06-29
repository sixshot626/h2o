package h2o.common.lang;

import h2o.apache.commons.lang.StringUtils;
import h2o.common.util.date.DateUtil;
import h2o.common.util.lang.StringUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Objects;

public final class SDateTime implements OptionalValue<String>, Comparable<SDateTime>, java.io.Serializable {

    private static final long serialVersionUID = 2203956607450263142L;

    public static final SDateTime NULL = new SDateTime();

    private static final String DATE_FMT = "yyyy-MM-dd'T'HH:mm:ss";


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
           Date date = null;
            if ( dateStr != null ) {
                date = toDate(dateStr, "yyyy-MM-dd HH:mm:ss");
                if ( date == null ) {
                    date = toDate(dateStr, DATE_FMT);
                }
            }
            this.value = date == null ? null : DateUtil.toString(date, DATE_FMT);
        }
    }


    public SDateTime(int year, int month, int day, int hour, int minute, int second) {
        this(StringUtil.build(
                StringUtils.leftPad(Integer.toString(year), 4, '0'), "-",
                StringUtils.leftPad(Integer.toString(month), 2, '0'), "-",
                StringUtils.leftPad(Integer.toString(day), 2, '0'), "T",
                StringUtils.leftPad(Integer.toString(hour), 2, '0'), ":",
                StringUtils.leftPad(Integer.toString(minute), 2, '0'), ":",
                StringUtils.leftPad(Integer.toString(second), 2, '0')));

    }

    public SDateTime(LocalDateTime localDateTime) {
        this.value = localDateTime == null ? null : StringUtil.build(
                StringUtils.leftPad(Integer.toString(localDateTime.getYear()), 4, '0'), "-",
                StringUtils.leftPad(Integer.toString(localDateTime.getMonthValue()), 2, '0'), "-",
                StringUtils.leftPad(Integer.toString(localDateTime.getDayOfMonth()), 2, '0'), "T",
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
        return new SDateTime(toDate(dateTime, fmt));
    }


    @Override
    public String getValue() {
        return this.value;
    }

    public SDate getDate() {

        if (this.isPresent()) {
            return SDate.from(this.value, DATE_FMT);
        } else {
            return new SDate();
        }

    }


    public STime getTime() {

        if (this.isPresent()) {
            return STime.from(this.value, DATE_FMT);
        } else {
            return new STime();
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
