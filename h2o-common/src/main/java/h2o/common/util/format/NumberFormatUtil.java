package h2o.common.util.format;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberFormatUtil {

    private NumberFormatUtil() {
    }

    public static String format(long number, String fmt) {
        return new DecimalFormat(fmt).format(number);
    }

    public static String format(double number, String fmt) {
        return new DecimalFormat(fmt).format(number);
    }

    public static String format(Object number, String fmt) {

        if (number instanceof String) {
            return new DecimalFormat(fmt).format(new BigDecimal((String) number));
        } else {
            return new DecimalFormat(fmt).format(number);
        }

    }


}
