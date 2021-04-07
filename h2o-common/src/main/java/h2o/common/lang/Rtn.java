package h2o.common.lang;

public final class Rtn<T> implements java.io.Serializable {

    private static final long serialVersionUID = 5164042784712875067L;
    
    public final boolean ok;
    public final NString msg;
    public final Val<T> value;

    public Rtn(boolean ok, NString msg, Val<T> value) {
        this.ok = ok;
        this.msg = msg;
        this.value = value;
    }

    public static <R> Rtn<R> ok() {
        return new Rtn<>( true , NString.NULL , Val.empty()  );
    }

    public static <R> Rtn<R> ok( R value ) {
        return new Rtn<>( true , NString.NULL , new Val<>(value)  );
    }

    public static <R> Rtn<R> err( String msg ) {
        return new Rtn<>( false , new NString(msg) , Val.empty()  );
    }

    public static <R> Rtn<R> err( String msg , R value ) {
        return new Rtn<>( false , new NString(msg) , new Val<>(value)  );
    }

}
