package h2o.common.result;

import h2o.common.lang.EBoolean;

import java.io.Serializable;

public class TransReturn<S, R> implements TransResponse<S, R>, TransStatus<S>, TransResult<R>, Response, ErrorInfo, Serializable {

    private static final long serialVersionUID = 2603355001138198223L;

    private EBoolean ok = EBoolean.Null;

    private EBoolean finalState = EBoolean.Null;

    private String code;

    private String msg;

    private Object status;

    private boolean hasResult;

    private Object result;

    private boolean hasException;

    private Throwable exception;

    public TransReturn() {
    }

    public TransReturn( Response response ) {


        this.ok                 = response.ok();
        this.finalState         = response.finalState();
        this.code               = response.getCode();
        this.msg                = response.getMsg();
        this.hasException       = response.hasException();
        this.exception          = response.getException();

        if ( response instanceof TransStatus ) {
            this.status         = ((TransStatus) response).getStatus();
        }

        if ( response instanceof TransResult ) {
            this.hasResult      = ((TransResult)response).hasResult();
            this.result         = ((TransResult)response).getResult();
        }

    }


    public TransReturn<S, R> error(String code, String msg) {

        this.ok(false);
        this.setCode(code);
        this.setMsg(msg);

        return this;
    }

    public TransReturn<S, R> error(ErrorInfo errorInfo) {

        this.ok(false);
        this.setCode(errorInfo.errorCode());
        this.setMsg(errorInfo.errorMsg());

        return this;
    }


    @Override
    public EBoolean ok() {
        return null;
    }

    @Override
    public EBoolean finalState() {
        return null;
    }

    @Override
    public boolean hasException() {
        return this.hasException;
    }

    @Override
    public boolean hasResult() {
        return this.hasResult;
    }


    public TransReturn<S, R> ok(TriState state) {
        this.ok = ( state == TriState.Unknown ? EBoolean.Null : EBoolean.valueOf( state == TriState.Success ) ) ;
        return this;
    }

    public TransReturn<S, R> finalState(TriState state) {
        this.finalState = ( state == TriState.Unknown ? EBoolean.Null : EBoolean.valueOf( state == TriState.Success ) ) ;;
        return this;
    }

    public TransReturn<S, R> ok(EBoolean ok) {
        this.ok = ok;
        return this;
    }

    public TransReturn<S, R> finalState(EBoolean finalState) {
        this.finalState = finalState;
        return this;
    }

    public TransReturn<S, R> ok(Boolean ok) {
        this.ok = EBoolean.valueOf( ok );
        return this;
    }

    public TransReturn<S, R> finalState(Boolean finalState) {
        this.finalState = EBoolean.valueOf( finalState );
        return this;
    }

    public TransReturn<S, R> exception() {
        this.hasException = true;
        return this;
    }

    public TransReturn<S, R> exception(Throwable exception) {
        this.exception = exception;
        this.hasException = true;
        return this;
    }

    public TransReturn<S, R> setCode(String code) {
        this.code = code;
        return this;
    }

    public TransReturn<S, R> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public TransReturn<S, R> setStatus(Object status) {
        this.status = status;
        return this;
    }

    public TransReturn<S, R> setResult(Object result) {
        this.result = result;
        this.hasResult = true;
        return this;
    }


    // Getter

    public EBoolean getOk() {
        return ok;
    }

    public EBoolean getFinalState() {
        return finalState;
    }

    public boolean isHasResult() {
        return hasResult;
    }

    public boolean isHasException() {
        return hasException;
    }

    @Override
    public Throwable getException() {
        return exception;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public S getStatus() {
        return (S) status;
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
    public boolean isPresent() {
        return result != null;
    }

    @Override
    public R getResult() {
        return (R) result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TransReturn{");
        sb.append("ok=").append(ok);
        sb.append(", finalState=").append(finalState);
        sb.append(", code='").append(code).append('\'');
        sb.append(", msg='").append(msg).append('\'');
        sb.append(", status=").append(status);
        sb.append(", hasResult=").append(hasResult);
        sb.append(", result=").append(result);
        sb.append(", hasException=").append(hasException);
        sb.append(", exception=").append(exception);
        sb.append('}');
        return sb.toString();
    }
}
