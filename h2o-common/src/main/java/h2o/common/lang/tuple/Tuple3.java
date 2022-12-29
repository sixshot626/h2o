package h2o.common.lang.tuple;

import java.util.Objects;

public final class Tuple3<A, B, C> implements Tuple {


    private static final long serialVersionUID = 8515896572423195899L;


    public final A e0;
    public final B e1;
    public final C e2;

    public Tuple3(A e0, B e1, C e2) {
        this.e0 = e0;
        this.e1 = e1;
        this.e2 = e2;
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


    public A _1() {
        return e0;
    }

    public B _2() {
        return e1;
    }

    public C _3() {
        return e2;
    }


    public int size() {
        return 3;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple3<?, ?, ?> tuple3 = (Tuple3<?, ?, ?>) o;
        return Objects.equals(e0, tuple3.e0) && Objects.equals(e1, tuple3.e1) && Objects.equals(e2, tuple3.e2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(e0, e1, e2);
    }

    @Override
    public String toString() {
        return String.format("Tuple3[e0=%s, e1=%s, e2=%s]", e0, e1, e2);
    }


    @SuppressWarnings("unchecked")
    public <T> T getE(int i) {
        if (i < 0 || i > 2) {
            throw new IndexOutOfBoundsException();
        }
        switch (i) {
            case 0:
                return (T) e0;
            case 1:
                return (T) e1;
            case 2:
                return (T) e2;
        }

        return null;
    }


}
