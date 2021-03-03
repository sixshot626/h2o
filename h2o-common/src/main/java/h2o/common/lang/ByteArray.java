package h2o.common.lang;

public class ByteArray implements NullableValue , java.io.Serializable {

    private static final long serialVersionUID = -5075649510738424502L;

    private final byte[] value;
    private final int offset;
    private final int size;


    public ByteArray() {
        this.value = null;
        this.offset = 0;
        this.size = 0;
    }

    public ByteArray( byte[] value ) {
        this.value = value;
        this.offset = 0;
        this.size = value == null ? 0 : value.length;
    }

    public ByteArray( byte[] value , int size ) {
        this.value = value;
        this.offset = 0;
        this.size = size;
    }

    public ByteArray( byte[] value , int offset , int size ) {
        this.value = value;
        this.offset = offset;
        this.size = size;
    }

    public ByteArray( byte[] src , int offset , int size , boolean copy ) {
        if ( copy ) {
            this.value = new byte[size];
            this.offset = 0;
            this.size = size;
            System.arraycopy(src, offset, this.value, 0, size);
        } else {
            this.value = src;
            this.offset = offset;
            this.size = size;
        }
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

    public int offset() {
        return offset;
    }

    public int size() {
        return size;
    }


    @Override
    public String toString() {

        if ( this.isPresent() ) {
            if ( offset == 0 ) {
                return "byte[" + offset + ".." + size + "]";
            } else {
                return "byte[" + size + "]";
            }
        } else {
            return "<null>";
        }

    }



}
