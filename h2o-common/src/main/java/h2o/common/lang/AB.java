package h2o.common.lang;

public final class AB<X,Y> implements java.io.Serializable {

    private static final long serialVersionUID = 7593389607204994788L;

    public final boolean ok;
    public final NString msg;
    public final Val<X> a;
    public final Val<Y> b;

    public AB( boolean ok, NString msg, Val<X> a, Val<Y> b ) {
        this.ok = ok;
        this.msg = msg;
        this.a = a;
        this.b = b;
    }

    public static <A,B>  AB<A,B> a( A a ) {
        return new AB<>(true , NString.NULL , new Val<>(a) , Val.empty() );
    }

    public static <A,B>  AB<A,B> a( A a , String msg ) {
        return new AB<>(true , new NString(msg) , new Val<>(a) , Val.empty() );
    }


    public static <A,B>  AB<A,B> b( B b ) {
        return new AB<>(false , NString.NULL , Val.empty() , new Val<>(b) );
    }

    public static <A,B>  AB<A,B> b( B b , String msg ) {
        return new AB<>(false , new NString(msg) , Val.empty() , new Val<>(b) );
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AB{");
        sb.append("ok=").append(ok);
        sb.append(", msg=").append(msg);
        sb.append(", a=").append(a);
        sb.append(", b=").append(b);
        sb.append('}');
        return sb.toString();
    }
}
