package h2o.common.lang;

public final class Pair<X,Y> implements java.io.Serializable {

    private static final long serialVersionUID = -3766442761149767884L;

    public final Val<X> a;
    public final Val<Y> b;

    public Pair(Val<X> a, Val<Y> b) {
        this.a = a;
        this.b = b;
    }


    public static <A,B>  Pair<A,B> empty() {
        return new Pair<>( Val.empty() , Val.empty() );
    }

    public static <A,B>  Pair<A,B> a( A a ) {
        return new Pair<>( new Val<>(a) , Val.empty() );
    }

    public static <A,B>  Pair<A,B> b( B b ) {
        return new Pair<>( Val.empty() , new Val<>(b) );
    }

    public static <A,B>  Pair<A,B> all( A a , B b ) {
        return new Pair<>( new Val<>(a) , new Val<>(b) );
    }






    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Pair{");
        sb.append("a=").append(a);
        sb.append(", b=").append(b);
        sb.append('}');
        return sb.toString();
    }
}
