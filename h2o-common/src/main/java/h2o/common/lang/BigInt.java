package h2o.common.lang;

public class BigInt implements java.io.Serializable {

    private static final long serialVersionUID = -6500041396268712115L;

    public long i;

    public BigInt() {
        this.i = 0L;
    }

    public BigInt(long i) {
        this.i = i;
    }
}
