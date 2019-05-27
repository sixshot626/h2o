package h2o.common.bean.result;

import java.io.Serializable;

/**
 * Created by zhangjianwei on 2017/6/7.
 */
public class TransResult<S,R> extends TransStatus<S> implements Serializable {

    private static final long serialVersionUID = -1448116426261181686L;

    public TransResult() {
        super();
    }

    public TransResult( TransStatus<S> transStatus ) {
        super(transStatus);
    }

    @Override
    public TransResult<S,R> from( TransResponse transResponse ) {
        super.from(transResponse);
        return this;
    }

    private R result;

    private Throwable e;


    @Override
    public R getResult() {
        return result;
    }

    public TransResult setResult( R result ) {
        this.result = result;
        return this;
    }

    public Throwable getE() {
        return e;
    }

    public TransResult setE(Throwable e) {
        this.e = e;
        return this;
    }

    @Override
    public TransResult<S,R> setFinalState(boolean finalState) {
        super.setFinalState(finalState);
        return this;
    }

    @Override
    public TransResult<S,R>  setSuccess(boolean success) {
        super.setSuccess(success);
        return this;
    }

    @Override
    public TransResult<S,R> setStatus(S status) {
        super.setStatus(status);
        return this;
    }


    @Override
    public TransResult<S,R> setCode(String code) {
        super.setCode(code);
        return this;
    }

    @Override
    public TransResult<S,R> setMsg(String msg) {
        super.setMsg(msg);
        return this;
    }

}
