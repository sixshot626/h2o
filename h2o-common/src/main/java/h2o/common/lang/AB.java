package h2o.common.lang;

public class AB<X,Y> implements java.io.Serializable {

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

    public static <R,S>  AB<R,S> a( Val<R> a ) {
        return new AB<>(true , NString.NULL , a , Val.empty() );
    }

    public static <R,S>  AB<R,S> a( Val<R> a , String msg ) {
        return new AB<>(true , new NString(msg) , a , Val.empty() );
    }


    public static <R,S>  AB<R,S> b( Val<S> b ) {
        return new AB<>(false , NString.NULL , Val.empty() , b );
    }

    public static <R,S>  AB<R,S> b( Val<S> b , String msg ) {
        return new AB<>(false , new NString(msg) , Val.empty() , b );
    }


    @Override
    public String toString() {
        return "AB{" +
                "ok=" + ok +
                ", msg=" + msg +
                ", a=" + a +
                ", b=" + b +
                '}';
    }
}
