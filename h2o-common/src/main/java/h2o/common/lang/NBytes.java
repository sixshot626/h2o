package h2o.common.lang;

import h2o.common.io.CharsetWrapper;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public final class NBytes implements NullableValue , java.io.Serializable {

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
    public boolean isPresent() {
        return this.value != null;
    }

    public byte[] getValue() {
        return this.value;
    }

    public byte[] get() {

        if ( this.isPresent() ) {
            return this.value;
        }

        throw new IllegalStateException();

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
