package h2o.common.result;

import h2o.common.lang.NString;

public class CallResult<E,R> implements java.io.Serializable {

    private final boolean success;

    private final E error;

    private final R content;

    public CallResult(boolean success, E error, R content) {
        this.success = success;
        this.error   = error;
        this.content = content;
    }

    public static <E,R> CallResult<E,R> success() {
        return new CallResult<>( true , null , null );
    }

    public static <E,R> CallResult<E,R> success(R content) {
        return new CallResult<>( true , null , content );
    }


    public static <E,R> CallResult<E,R> error() {
        return new CallResult<>( false , null , null );
    }

    public static <E,R> CallResult<E,R> error(E error) {
        return new CallResult<>( false , error , null );
    }



    public boolean isSuccess() {
        return success;
    }

    public E getError() {
        return error;
    }

    public R getContent() {
        return content;
    }



    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CallResult{");
        sb.append("success=").append(success);
        sb.append(", error=").append(error);
        sb.append(", content=").append(content);
        sb.append('}');
        return sb.toString();
    }
}
