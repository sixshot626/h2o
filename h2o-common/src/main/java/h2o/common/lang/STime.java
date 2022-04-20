package h2o.common.lang;

import h2o.apache.commons.lang.StringUtils;
import h2o.common.util.date.DateUtil;
import h2o.common.util.lang.StringUtil;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

public final class STime implements OptionalValue<String>, Comparable<STime>, java.io.Serializable {

    private static final long serialVersionUID = -1955168203520594449L;

    public static final STime NULL = new STime();

    private static final String DATE_FMT = "HH:mm:ss";


    /**
     * 时间 HH:mm:ss
     */
    private final String value;

    public STime() {
        this.value = null;
    }

    public STime(String time) {
        this(time, false);
    }

    public STime(String timeStr, boolean direct) {
        if (direct) {
            this.value = timeStr;
        } else {
            this.value = timeStr == null ? null : DateUtil.toString(toDate(timeStr, DATE_FMT), DATE_FMT);
        }
    }

    public STime(int hour, int minute, int second) {
        this(StringUtil.build(StringUtils.leftPad(Integer.toString(hour), 2, '0'), ":",
                StringUtils.leftPad(Integer.toString(minute), 2, '0'), ":",
                StringUtils.leftPad(Integer.toString(second), 2, '0')));
    }

    public STime(LocalTime localTime) {
        this.value = localTime == null ? null : StringUtil.build(
                StringUtils.leftPad(Integer.toString(localTime.getHour()), 2, '0'), ":",
                StringUtils.leftPad(Integer.toString(localTime.getMinute()), 2, '0'), ":",
                StringUtils.leftPad(Integer.toString(localTime.getSecond()), 2, '0'));
    }

    public STime(Instant instant) {
        this.value = instant == null ? null : DateUtil.toString(new Date(instant.toEpochMilli()), DATE_FMT);
    }

    public STime(Date d) {
        this.value = d == null ? null : DateUtil.toString(d, DATE_FMT);
    }

    public STime(STime stime) {
        this.value = stime.value;
    }


    private static Date toDate(String date, String fmt) {
        return SDate.toDate(date, fmt);
    }

    public static STime from(String time, String fmt) {
        return new STime(toDate(time, fmt));
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


    public LocalTime toLocalTime() {
        return LocalTime.of(this.getHour(), this.getMinute(), this.getSecond());
    }


    public int getHour() {
        return Integer.parseInt(StringUtils.substringBefore(this.get(), ":"));
    }

    public int getMinute() {
        return Integer.parseInt(StringUtils.substringBetween(this.get(), ":"));
    }

    public int getSecond() {
        return Integer.parseInt(StringUtils.substringAfterLast(this.get(), ":"));
    }


    @Override
    public int compareTo(STime other) {

        String l = this.orElse("");
        String r = other.orElse("");

        return l.compareTo(r);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        STime sTime = (STime) o;
        return Objects.equals(value, sTime.value);
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
