package h2o.common.lang;

import h2o.common.exception.ExceptionUtil;
import h2o.common.io.CharsetWrapper;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public final class NBytes implements OptionalValue<byte[]>, java.io.Serializable {

    private static final long serialVersionUID = 180228261982672556L;

    public static final NBytes NULL = new NBytes();

    private final byte[] value;

    public NBytes() {
        this.value = null;
    }

    public NBytes(byte[] value) {
        this.value = value;
    }

    public static NBytes from(String str) {
        return new NBytes(str == null ? null : str.getBytes(StandardCharsets.UTF_8));
    }

    public static NBytes from(String str, CharsetWrapper charsetWrapper) {
        try {
            return new NBytes(str == null ? null : str.getBytes(charsetWrapper.charset));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public byte[] getValue() {
        return this.value;
    }

    public String buildString() {
        return new String(this.get(), StandardCharsets.UTF_8);
    }

    public String buildString(CharsetWrapper charsetWrapper) {
        try {
            return new String(this.get(), charsetWrapper.charset);
        } catch (UnsupportedEncodingException e) {
            throw ExceptionUtil.toRuntimeException(e);
        }
    }


    @Override
    public String toString() {

        if (this.isPresent()) {
            return "byte[" + this.value.length + "]";
        } else {
            return "<null>";
        }

    }


}
