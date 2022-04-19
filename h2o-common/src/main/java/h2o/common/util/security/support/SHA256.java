package h2o.common.util.security.support;

import h2o.common.util.security.Encryptor;
import h2o.common.util.security.MessageDigestUtil;


public class SHA256 implements Encryptor {

    private final String charset;

    public SHA256() {
        this.charset = null;
    }

    public SHA256(String charset) {
        this.charset = charset;
    }


    public String enc(String str) {
        return MessageDigestUtil.digest("SHA-256", str, charset);
    }

}