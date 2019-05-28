package h2o.common.bean.result;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by zhangjianwei on 2017/6/7.
 */
public class TransStatus<S> extends TransResponse implements ErrorInfo , Serializable {

    private static final long serialVersionUID = -8626841949944329920L;

    private S status;

    public TransStatus() {
    }

    public TransStatus( TransStatus<S> transStatus ) {
        super( transStatus );
        this.setStatus( transStatus.getStatus() );
    }

    public TransStatus<S> from( TransResponse transResponse ) {
        this.setFinalState( transResponse.isFinalState() );
        this.setSuccess( transResponse.isSuccess() );
        this.setCode( transResponse.getCode() );
        this.setMsg( transResponse.getMsg() );
        return this;
    }

    @Override
    public TransStatus<S> error(String code, String msg) {
        super.error(code, msg);
        return this;
    }

    @Override
    public TransStatus<S> error( ErrorInfo errorInfo ) {
        super.error(errorInfo);
        return this;
    }


    public TransStatus<S> setFinalState(boolean finalState) {
        super.setFinalState(finalState);
        return this;
    }



    public TransStatus<S> setSuccess(boolean success) {
        super.setSuccess(success);
        return this;
    }

    public S getStatus() {
        return status;
    }

    public TransStatus<S> setStatus(S status) {
        this.status = status;
        return this;
    }

    public TransStatus<S> setCode(String code) {
        super.setCode( code );
        return this;
    }

    public TransStatus<S> setMsg(String msg) {
        super.setMsg( msg );
        return this;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
