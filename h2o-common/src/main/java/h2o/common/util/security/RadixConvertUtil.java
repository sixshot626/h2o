package h2o.common.util.security;

import java.math.BigInteger;

public class RadixConvertUtil {

    private RadixConvertUtil() {
    }

    public static final String RADIX64 = "0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ_*";


    public static String hexTo64(String hexStr) {
        return hexTo64(hexStr, RADIX64);
    }

    public static String hexTo64(String hexStr, String str64) {

        if (str64 == null) {
            str64 = RADIX64;
        }

        String ocxStr = new BigInteger(hexStr, 16).toString(8);
        if (ocxStr.length() % 2 != 0) {
            ocxStr = "0" + ocxStr;
        }

        char[] ocxChars = ocxStr.toCharArray();

        StringBuilder s64 = new StringBuilder();
        for (int i = 0, len = ocxChars.length; i < len; i += 2) {
            s64.append(str64.charAt(Integer.parseInt(new String(new char[]{ocxChars[i], ocxChars[i + 1]}), 8)));
        }


        return s64.toString();
    }


    public static String a2b(String v, int ra, int rb) {

        return new BigInteger(v, ra).toString(rb);

    }


}
