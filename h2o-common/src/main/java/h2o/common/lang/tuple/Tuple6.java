package h2o.common.lang.tuple;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public final class Tuple6<A, B, C, D, E, F> implements Tuple {


    private static final long serialVersionUID = 8262800490705223200L;

    public final A e0;
    public final B e1;
    public final C e2;
    public final D e3;
    public final E e4;
    public final F e5;

    public Tuple6(A e0, B e1, C e2, D e3, E e4, F e5) {
        this.e0 = e0;
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
        this.e4 = e4;
        this.e5 = e5;
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

    public E getE4() {
        return e4;
    }

    public F getE5() {
        return e5;
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

    public E _5() {
        return e4;
    }

    public F _6() {
        return e5;
    }


    public int size() {
        return 6;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Tuple6<?, ?, ?, ?, ?, ?> tuple6 = (Tuple6<?, ?, ?, ?, ?, ?>) o;

        return new EqualsBuilder()
                .append(e0, tuple6.e0)
                .append(e1, tuple6.e1)
                .append(e2, tuple6.e2)
                .append(e3, tuple6.e3)
                .append(e4, tuple6.e4)
                .append(e5, tuple6.e5)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(e0)
                .append(e1)
                .append(e2)
                .append(e3)
                .append(e4)
                .append(e5)
                .toHashCode();
    }

    @Override
    public String toString() {
        return String.format("Tuple6[e0=%s, e1=%s, e2=%s, e3=%s, e4=%s, e5=%s]", e0, e1, e2, e3, e4, e5);
    }

    @SuppressWarnings("unchecked")
    public <T> T getE(int i) {
        if (i < 0 || i > 5) {
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
            case 4:
                return (T) e4;
            case 5:
                return (T) e5;
        }

        return null;
    }

}
