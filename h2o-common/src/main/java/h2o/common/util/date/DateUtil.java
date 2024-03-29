package h2o.common.util.date;

import java.util.Date;


public abstract class DateUtil {


    public static final class Format {

        private Format() {}

        /**
         * yyyy-MM-dd
         */
        public static final String Y_M_D = "yyyy-MM-dd";

        /**
         *yyyyMMdd
         */
        public static final String YMD = "yyyyMMdd";

        /**
         * HH:mm:ss
         */
        public static final String H_M_S = "HH:mm:ss";

        /**
         * HHmmss
         */
        public static final String HMS = "HHmmss";

        /**
         * yyyy-MM-dd HH:mm:ss
         */
        public static final String D_S = "yyyy-MM-dd HH:mm:ss";

        /**
         * yyyy-MM-dd HH:mm:ss.SSS
         */
        public static final String D_S_S = "yyyy-MM-dd HH:mm:ss.SSS";

        /**
         * yyyy-MM-dd'T'HH:mm:ss
         */
        public static final String D_T_S = "yyyy-MM-dd'T'HH:mm:ss";

        /**
         * yyyy-MM-dd'T'HH:mm:ss.SSS
         */
        public static final String D_T_S_S = "yyyy-MM-dd'T'HH:mm:ss.SSS";

        /**
         * yyyyMMddHHmmss
         */
        public static final String DS = "yyyyMMddHHmmss";

        /**
         * yyyyMMddHHmmssSSS
         */
        public static final String DSS = "yyyyMMddHHmmssSSS";

    }



    private DateUtil() {
    }


    private final static DateTime dateTime = new DateTime(false);


    public static String toString(Date d) {
        return dateTime.toString(d);
    }

    public static String toShortString(Date d) {
        return dateTime.toShortString(d);
    }

    public static String toLongString(Date d) {
        return dateTime.toLongString(d);
    }

    public static String toTimeString(Date d) {
        return dateTime.toTimeString(d);
    }

    public static String toString(Date d, String fmt) {
        return dateTime.toString(d, fmt);
    }


    public static Date toDate(String sd) {
        return dateTime.toDate(sd);
    }

    public static Date toDate(String sd, String fmt) {
        return dateTime.toDate(sd, fmt);
    }

    public static String str2Str(String sd, String sfmt, String tfmt) {
        return dateTime.str2Str(sd, sfmt, tfmt);
    }


    public static int getDaysOfMonth(String year, String month) {
        return dateTime.getDaysOfMonth(year, month);
    }

    public static int getDaysBetween(Date date_start, Date date_end) {
        return dateTime.getDaysBetween(date_start, date_end);
    }

    public static int getMonthsBetween(Date date_start, Date date_end) {
        return dateTime.getMonthsBetween(date_start, date_end);
    }

    public static int getActualMaximum(Date date) {
        return dateTime.getActualMaximum(date);
    }


    public static Date getAfterDay(Date date, int count) {
        return dateTime.getAfterDay(date, count);
    }

    public static Date getAfterDate(Date date, int type, int count) {
        return dateTime.getAfterDate(date, type, count);
    }

}
