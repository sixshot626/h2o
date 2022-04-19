package h2o.common.util.id;

import h2o.common.util.security.RadixConvertUtil;
import h2o.apache.commons.lang.StringUtils;


public class UuidUtil {

    private UuidUtil() {
    }


    public static String getUuid() {
        return java.util.UUID.randomUUID().toString();
    }

    public static String getUuid16() {
        return getUuid().replace("-", "");
    }

    public static String getUuid64() {
        return to64(getUuid16());
    }

    public static String getUuid64(String str64) {
        return to64(getUuid16(), str64);
    }


    public static String to64(String uuid16) {
        return StringUtils.leftPad(RadixConvertUtil.hexTo64(uuid16), 22, '0');
    }

    public static String to64(String uuid16, String str64) {
        return StringUtils.leftPad(RadixConvertUtil.hexTo64(uuid16, str64), 22, '0');
    }

}
