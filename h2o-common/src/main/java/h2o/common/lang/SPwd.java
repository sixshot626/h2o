package h2o.common.lang;

import h2o.common.util.security.EncryptUtil;

public class SPwd {

    private final String value;

    public SPwd(String str , String key ) {
        if (str == null || key == null ) {
            throw new IllegalArgumentException();
        }
        this.value = EncryptUtil.decrypt(str , key);
    }


    public static String encrypt( String pwd, String key) {
        return EncryptUtil.encrypt( pwd , key );
    }


    public String getValue() {
        return value;
    }


}
