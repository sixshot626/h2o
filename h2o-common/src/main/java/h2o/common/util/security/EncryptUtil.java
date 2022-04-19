package h2o.common.util.security;

import h2o.common.util.security.support.MD5;
import h2o.common.util.security.support.SimpleSeasoner;

public class EncryptUtil {

    private Encryptor encryptor;
    private Seasoner seasoner;

    public void setEncryptor(Encryptor encryptor) {
        this.encryptor = encryptor;
    }

    public void setSeasoner(Seasoner seasoner) {
        this.seasoner = seasoner;
    }

    public EncryptUtil() {
    }

    public EncryptUtil(Encryptor encryptor) {
        this.setEncryptor(encryptor);
    }

    public EncryptUtil(Encryptor encryptor, Seasoner seasoner) {
        this.setEncryptor(encryptor);
        this.setSeasoner(seasoner);
    }

    private static EncryptUtil staticEncryptUtil;

    public static EncryptUtil getStaticEncryptUtil() {
        if (staticEncryptUtil == null) {
            staticEncryptUtil = new EncryptUtil(new MD5(), new SimpleSeasoner());
        }
        return staticEncryptUtil;
    }

    public String enc(String str) {
        return encryptor.enc(str);
    }

    public String enc(String str, String salt) {
        return encryptor.enc(seasoner.season(str, salt));
    }

}
