package h2o.common.lang;

public final class Res<T> implements  java.io.Serializable {

    private static final long serialVersionUID = -2263275208439612819L;

    public final boolean ok;
    public final NString msg;
    public final Val<T> value;

    private Res(boolean ok, NString msg, Val<T> value) {
        this.ok = ok;
        this.msg = msg;
        this.value = value;
    }

    public static <R> Res<R> ok() {
        return new Res<>( true , NString.NULL , Val.empty()  );
    }

    public static <R> Res<R> ok( R value ) {
        return new Res<>( true , NString.NULL , new Val<>(value)  );
    }

    public static <R> Res<R> err( String msg ) {
        return new Res<>( false , new NString(msg) , Val.empty()  );
    }

    public static <R> Res<R> err( String msg , R value ) {
        return new Res<>( false , new NString(msg) , new Val<>(value)  );
    }

}
