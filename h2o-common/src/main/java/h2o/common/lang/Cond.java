package h2o.common.lang;

import java.util.Objects;

public class Cond<T> implements java.io.Serializable {

    private static final Cond<?> EMPTY = new Cond<>(false , null);

    public final boolean ok;

    public final T value;

    public Cond(boolean ok, T value) {
        this.ok = ok;
        this.value = value;
    }

    public static <R> Cond<R> ok(R value) {
        return new Cond<>(true,value);
    }

    public static <R> Cond<R> empty() {
        @SuppressWarnings("unchecked")
        Cond<R> t = (Cond<R>) EMPTY;
        return t;
    }


    public boolean isOk() {
        return ok;
    }

    public T getValue() {
        return value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cond<?> cond = (Cond<?>) o;
        return ok == cond.ok && Objects.equals(value, cond.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ok, value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Cond{");
        sb.append("ok=").append(ok);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
