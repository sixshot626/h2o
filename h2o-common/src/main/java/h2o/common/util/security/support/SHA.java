package h2o.common.util.security.support;

import h2o.common.util.security.Encryptor;
import h2o.common.util.security.MessageDigestUtil;


public class SHA implements Encryptor {

    private final String charset;

    public SHA() {
        this.charset = null;
    }

    public SHA(String charset) {
        this.charset = charset;
    }

    @Override
    public String enc(String str) {
        return MessageDigestUtil.digest("SHA", str, charset);
    }

}