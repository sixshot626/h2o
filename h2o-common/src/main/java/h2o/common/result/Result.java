package h2o.common.result;

public class Result<E,R> implements java.io.Serializable {

    private final boolean success;

    private final E error;

    private final R content;

    public Result(boolean success, E error, R content) {
        this.success = success;
        this.error   = error;
        this.content = content;
    }

    public static <E,R> Result<E,R> success() {
        return new Result<>( true , null , null );
    }

    public static <E,R> Result<E,R> success(R content) {
        return new Result<>( true , null , content );
    }


    public static <E,R> Result<E,R> fail() {
        return new Result<>( false , null , null );
    }

    public static <E,R> Result<E,R> fail(E error) {
        return new Result<>( false , error , null );
    }

    public static <E,R> Result<E,R> fail(E error , R content) {
        return new Result<>( false , error , content );
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
        final StringBuilder sb = new StringBuilder("Result{");
        sb.append("success=").append(success);
        sb.append(", error=").append(error);
        sb.append(", content=").append(content);
        sb.append('}');
        return sb.toString();
    }
}
