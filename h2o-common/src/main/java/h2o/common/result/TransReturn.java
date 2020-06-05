package h2o.common.result;

public class TransReturn<S,R> implements TransResponse<S,R>, TransStatus<S>, TransResult<R>, Response, ErrorInfo , java.io.Serializable {

    private boolean finalState;

    private boolean success;

    private String code;

    private String msg;

    private Object status;

    private Throwable e;

    private Object result;

    public TransReturn() {}

    public TransReturn(Response response ) {

        this.finalState = response.isFinalState();
        this.success = response.isSuccess();
        this.code = response.getCode();
        this.msg = response.getMsg();
        this.e = response.getE();

        if ( response instanceof TransStatus) {
            Object status = ((TransStatus)response).getStatus();
            this.status = status;
        }

        if ( response instanceof TransResult) {
            Object result = ((TransResult)response).getResult();
            this.result = result;
        }

    }


    public TransReturn<S,R> error(String code , String msg ) {

        this.setSuccess( false );
        this.setCode( code );
        this.setMsg( msg );

        return this;
    }

    public TransReturn<S,R> error(ErrorInfo errorInfo ) {

        this.setSuccess( false );
        this.setCode( errorInfo.errorCode() );
        this.setMsg( errorInfo.errorMsg() );

        return this;
    }


    @Override
    public boolean isFinalState() {
        return finalState;
    }

    public TransReturn<S,R> setFinalState(boolean finalState) {
        this.finalState = finalState;
        return this;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    public TransReturn<S,R> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    @Override
    public String getCode() {
        return code;
    }

    public TransReturn<S,R> setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public TransReturn<S,R> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    @Override
    public S getStatus() {
        return (S)status;
    }

    public TransReturn<S,R> setStatus(Object status) {
        this.status = status;
        return this;
    }

    @Override
    public String errorCode() {
        return code;
    }

    @Override
    public String errorMsg() {
        return msg;
    }

    @Override
    public boolean isException() {
        return e != null;
    }

    @Override
    public Throwable getE() {
        return e;
    }

    public TransReturn<S,R> setE(Throwable e) {
        this.e = e;
        return this;
    }

    @Override
    public R getResult() {
        return (R)result;
    }

    public TransReturn<S,R> setResult(Object result) {
        this.result = result;
        return this;
    }
}
