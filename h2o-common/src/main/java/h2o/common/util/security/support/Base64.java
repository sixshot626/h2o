package h2o.common.util.security.support;

import h2o.common.util.security.Base64Util;
import h2o.common.util.security.Encryptor;

public class Base64 implements Encryptor {

    private final String charset;

    private final Base64Util b64Util;

    public Base64() {
        this.charset = null;
        this.b64Util = new Base64Util();
    }

    public Base64(String charset) {
        this.charset = charset;
        this.b64Util = new Base64Util();
    }

    public Base64(String charset, Base64Util b64Util) {
        this.charset = charset;
        this.b64Util = b64Util;
    }

    @Override
    public String enc(String str) {
        return charset == null ? b64Util.encode(str) : b64Util.encode(str, charset);
    }

}
