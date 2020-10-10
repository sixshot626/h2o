package h2o.common.result;

import java.io.Serializable;
import java.util.Optional;

public class TransReturn<S,R> implements TransResponse<S,R>, TransStatus<S>, TransResult<R>, Response, ErrorInfo , Serializable {

    private static final long serialVersionUID = 2603355001138198223L;

    private Boolean finalState;

    private Boolean success;

    private String code;

    private String msg;

    private Object status;

    private boolean presentResult;

    private Object result;

    private Throwable e;

    public TransReturn() {}

    public TransReturn( Response response ) {

        this.finalState         = response.isFinalState().orElse(null);
        this.success            = response.isSuccess().orElse(null);
        this.code               = response.getCode();
        this.msg                = response.getMsg();
        this.e                  = response.getE();
        this.presentResult      = response.isPresentResult();
        this.result             = response.getResult();

        if ( response instanceof TransStatus) {
            Object status = ((TransStatus)response).getStatus();
            this.status = status;
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
    public Optional<Boolean> isFinalState() {
        return Optional.ofNullable(finalState);
    }

    public TransReturn<S,R> setFinalState(boolean finalState) {
        this.finalState = Boolean.valueOf(finalState);
        return this;
    }

    @Override
    public Optional<Boolean> isSuccess() {
        return Optional.ofNullable(success);
    }

    public TransReturn<S,R> setSuccess(boolean success) {
        this.success = Boolean.valueOf(success);
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
    public boolean isPresentResult() {
        return presentResult;
    }

    @Override
    public R getResult() {
        return (R)result;
    }

    public TransReturn<S,R> setResult(Object result) {
        this.result = result;
        this.presentResult = true;
        return this;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TransReturn{");
        sb.append("finalState=").append(finalState);
        sb.append(", success=").append(success);
        sb.append(", code='").append(code).append('\'');
        sb.append(", msg='").append(msg).append('\'');
        sb.append(", status=").append(status);
        sb.append(", presentResult=").append(presentResult);
        sb.append(", result=").append(result);
        sb.append(", e=").append(e);
        sb.append('}');
        return sb.toString();
    }
}
