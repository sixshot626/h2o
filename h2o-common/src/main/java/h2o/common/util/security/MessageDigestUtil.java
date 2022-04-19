package h2o.common.util.security;

import h2o.common.exception.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

public final class MessageDigestUtil {

    private static final Logger log = LoggerFactory.getLogger(MessageDigestUtil.class.getName());

    private MessageDigestUtil() {
    }

    public static String digest(String mdName, String str, String charset) {

        try {

            MessageDigest md = MessageDigest.getInstance(mdName);

            return HexUtil.byteArrayToHexString(md.digest(charset == null ? str.getBytes() : str.getBytes(charset)));

        } catch (Exception e) {
            log.debug("", e);
            throw ExceptionUtil.toRuntimeException(e);
        }

    }


}
