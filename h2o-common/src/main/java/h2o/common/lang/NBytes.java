package h2o.common.lang;

import h2o.common.io.CharsetWrapper;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

public final class NBytes implements OptionalValue<byte[]>, java.io.Serializable {

    private static final long serialVersionUID = 180228261982672556L;

    public static final NBytes NULL = new NBytes();

    private final byte[] value;

    public NBytes() {
        this.value = null;
    }

    public NBytes( byte[] value ) {
        this.value = value;
    }

    public NBytes( String str ) {
        this.value = str == null ? null : str.getBytes(StandardCharsets.UTF_8);
    }

    public NBytes( String str , CharsetWrapper charset ) {
        try {
            this.value = str == null ? null : str.getBytes( charset.charset );
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public byte[] getValue() {
        return this.value;
    }

    @Override
    public String toString() {

        if ( this.isPresent() ) {
            return "byte[" + this.value.length + "]";
        } else {
            return "<null>";
        }

    }



}
