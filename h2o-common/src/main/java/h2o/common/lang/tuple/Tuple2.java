package h2o.common.lang.tuple;

import java.util.Objects;

public final class Tuple2<A, B> implements Tuple {

    private static final long serialVersionUID = 5786178318377966323L;

    public final A e0;
    public final B e1;

    public Tuple2(A e0, B e1) {
        this.e0 = e0;
        this.e1 = e1;
    }

    public A getE0() {
        return e0;
    }

    public B getE1() {
        return e1;
    }

    public A _1() {
        return e0;
    }

    public B _2() {
        return e1;
    }

    public int size() {
        return 2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;
        return Objects.equals(e0, tuple2.e0) && Objects.equals(e1, tuple2.e1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(e0, e1);
    }

    @Override
    public String toString() {
        return String.format("Tuple2[e0=%s, e1=%s]", e0, e1);
    }

    @SuppressWarnings("unchecked")
    public <T> T getE(int i) {
        if (i < 0 || i > 1) {
            throw new IndexOutOfBoundsException();
        }
        switch (i) {
            case 0:
                return (T) e0;
            case 1:
                return (T) e1;
        }

        return null;
    }


}
