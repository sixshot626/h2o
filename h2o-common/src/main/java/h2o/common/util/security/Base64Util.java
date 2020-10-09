package h2o.common.util.security;

import h2o.common.collection.ConcurrentBiMap;
import h2o.common.exception.ExceptionUtil;
import h2o.jodd.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class Base64Util {

    private static final Logger log = LoggerFactory.getLogger( Base64Util.class.getName() );

    private static final String STD_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

	public final String CHARS;

	private final ConcurrentBiMap<Character, Character> bidi;

	public Base64Util() {
		CHARS = STD_CHARS;
		bidi = null;
	}

	public Base64Util(String s64) {

		if (s64 == null || s64.length() != 65) {
			throw new IllegalArgumentException();
		}

		CHARS = s64;

		bidi = new ConcurrentBiMap<Character, Character>();

		for (int i = 0; i < 65; i++) {
			bidi.put(CHARS.charAt(i), STD_CHARS.charAt(i));
		}
		bidi.readonlyMode();

	}

	private String m2s(String my64) {

		if (bidi == null) {
			return my64;
		}

		char[] s64 = new char[my64.length()];

		for (int i = 0; i < my64.length(); i++) {
			s64[i] = (Character) bidi.get(my64.charAt(i));
		}

		return new String(s64);

	}

	private String s2m(String s64) {

		if (bidi == null) {
			return s64;
		}

		char[] m64 = new char[s64.length()];

		for (int i = 0; i < s64.length(); i++) {
			m64[i] = (Character) bidi.getKey(s64.charAt(i));
		}

		return new String(m64);

	}

	public String encode(byte[] raw) {
		return s2m(Base64.encodeToString(raw));
	}

	public byte[] decode(String base64) {
		return Base64.decode(m2s(base64));
	}

	public String encode(String s) {
		return encode(s, "UTF-8");
	}

	public String encode(String s, String charset) {
		try {
			return encode(s.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			log.debug("encode", e);
			throw ExceptionUtil.toRuntimeException(e);
		}

	}

	public String decode2s(String b64) {
		return decode2s(b64, "UTF-8");
	}

	public String decode2s(String b64, String charset) {
		try {
			return new String(decode(b64), charset);
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
