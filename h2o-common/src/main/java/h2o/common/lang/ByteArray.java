package h2o.common.lang;

public final class ByteArray implements NullableValue , java.io.Serializable {

    private static final long serialVersionUID = 1620318712792869290L;

    public static final ByteArray NULL = new ByteArray();


    private final byte[] value;

    public ByteArray() {
        this.value = null;
    }

    public ByteArray( byte[] value ) {
        this.value = value;
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
