package h2o.common.lang;

import java.util.Objects;

public class BigInt implements java.io.Serializable {

    private static final long serialVersionUID = -6500041396268712115L;

    public long i;

    public BigInt() {
        this.i = 0L;
    }

    public BigInt(long i) {
        this.i = i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BigInt bigInt = (BigInt) o;
        return i == bigInt.i;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i);
    }

    @Override
    public String toString() {
        return Long.toString(i);
    }

}
