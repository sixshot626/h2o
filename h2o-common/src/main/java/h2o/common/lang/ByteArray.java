package h2o.common.lang;

public class ByteArray implements NullableValue , java.io.Serializable {

    private static final long serialVersionUID = -5075649510738424502L;

    private final byte[] value;

    public ByteArray() {
        this.value = null;
    }

    public ByteArray( byte[] value ) {
        this.value = value;
    }

    @Override
    public boolean isPresent() {
        return value != null;
    }

    public byte[] getValue() {
        return value;
    }

    public byte[] get() {

        if ( this.isPresent() ) {
            return value;
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
