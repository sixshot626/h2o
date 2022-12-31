package h2o.common.lang.tuple;

import java.util.Objects;

public final class Tuple4<A, B, C, D> implements Tuple {


    private static final long serialVersionUID = -2006381057796189770L;

    public final A e0;
    public final B e1;
    public final C e2;
    public final D e3;

    public Tuple4(A e0, B e1, C e2, D e3) {
        this.e0 = e0;
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }


    public A getE0() {
        return e0;
    }

    public B getE1() {
        return e1;
    }

    public C getE2() {
        return e2;
    }

    public D getE3() {
        return e3;
    }


    public A _1() {
        return e0;
    }

    public B _2() {
        return e1;
    }

    public C _3() {
        return e2;
    }

    public D _4() {
        return e3;
    }

    @Override
    public int size() {
        return 4;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple4<?, ?, ?, ?> tuple4 = (Tuple4<?, ?, ?, ?>) o;
        return Objects.equals(e0, tuple4.e0) && Objects.equals(e1, tuple4.e1) && Objects.equals(e2, tuple4.e2) && Objects.equals(e3, tuple4.e3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(e0, e1, e2, e3);
    }

    @Override
    public String toString() {
        return String.format("Tuple4[e0=%s, e1=%s, e2=%s, e3=%s]", e0, e1, e2, e3);
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> T getE(int i) {
        if (i < 0 || i > 3) {
            throw new IndexOutOfBoundsException();
        }
        switch (i) {
            case 0:
                return (T) e0;
            case 1:
                return (T) e1;
            case 2:
                return (T) e2;
            case 3:
                return (T) e3;
        }

        return null;
    }


}
