package h2o.common.util.security;

import h2o.common.collection.ConcurrentBiMap;
import h2o.common.exception.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class Base32Util {

    private static final Logger log = LoggerFactory.getLogger(Base32Util.class.getName());

    private static final String STD_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567=";


    public final String CHARS;

    private final ConcurrentBiMap<Character, Character> bidi;


    public Base32Util() {
        CHARS = STD_CHARS;
        bidi = null;
    }

    public Base32Util(String s32) {

        if (s32 == null || s32.length() != 33) {
            throw new IllegalArgumentException();
        }

        CHARS = s32;

        bidi = new ConcurrentBiMap<Character, Character>();

        for (int i = 0; i < 33; i++) {
            bidi.put(CHARS.charAt(i), STD_CHARS.charAt(i));
        }
        bidi.readonlyMode();

    }


    private String m2s(String my32) {

        if (bidi == null) {
            return my32;
        }

        char[] s32 = new char[my32.length()];

        for (int i = 0; i < my32.length(); i++) {
            s32[i] = (Character) bidi.get(my32.charAt(i));
        }

        return new String(s32);

    }

    private String s2m(String s32) {

        if (bidi == null) {
            return s32;
        }

        char[] m32 = new char[s32.length()];

        for (int i = 0; i < s32.length(); i++) {
            m32[i] = (Character) bidi.getKey(s32.charAt(i));
        }

        return new String(m32);

    }


    public String encode(byte[] raw) {
        return s2m(Base32.encode(raw));
    }


    public byte[] decode(String base32) {
        return Base32.decode(m2s(base32));
    }


    public String encode(String s) {
        return encode(s, "UTF-8");
    }

    public String encode(String s, String charSet) {
        try {
            return encode(s.getBytes(charSet));
        } catch (UnsupportedEncodingException e) {
            log.debug("encode", e);
            throw ExceptionUtil.toRuntimeException(e);
        }

    }


    public String decode2s(String b32) {
        return decode2s(b32, "UTF-8");
    }

    public String decode2s(String b32, String charSet) {
        try {
            return new String(decode(b32), charSet);
        } catch (UnsupportedEncodingException e) {
            log.debug("decode2s", e);
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

    public String objectToString(Object o) {
        if (o == null) {
            return null;
        }

        ByteArrayOutputStream bam = new ByteArrayOutputStream();

        try {
            ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(bam));

            os.flush();
            os.writeObject(o);
            os.flush();

            return encode(bam.toByteArray());
        } catch (IOException e) {
            log.debug("objectToString", e);
            throw ExceptionUtil.toRuntimeException(e);
        }

    }

    public Object stringToObject(String s) {
        if (s == null) {
            return null;
        }

        byte bytes[] = decode(s);

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

        try {
            ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(bais));

            return is.readObject();
        } catch (Exception e) {
            log.debug("stringToObject", e);
            throw ExceptionUtil.toRuntimeException(e);
        }

    }

}
