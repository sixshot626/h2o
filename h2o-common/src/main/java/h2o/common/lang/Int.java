package h2o.common.lang;

import java.util.Objects;

public final class Int implements java.io.Serializable {

    private static final long serialVersionUID = 8952205408562577641L;

    public int i;

    public Int() {
        this.i = 0;
    }

    public Int(int i) {
        this.i = i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Int anInt = (Int) o;
        return i == anInt.i;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i);
    }

    @Override
    public String toString() {
        return Integer.toString(i);
    }
}
