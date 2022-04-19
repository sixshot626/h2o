package h2o.common.util.security.support;

import h2o.common.util.security.Base64Util;
import h2o.common.util.security.Encryptor;

public class Base64 implements Encryptor {

    private final String charset;

    public Base64() {
        this.charset = null;
    }

    public Base64(String charset) {
        this.charset = charset;
    }

    private volatile Base64Util b64Util = new Base64Util();

    public Base64 setBase64Util(Base64Util b64Util) {
        this.b64Util = b64Util;
        return this;
    }


    public String enc(String str) {
        return charset == null ? b64Util.encode(str) : b64Util.encode(str, charset);
    }

}
