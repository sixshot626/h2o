package h2o.common.bean.result;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class TransResponse implements ErrorInfo , Serializable {

    private static final long serialVersionUID = -4600331310699408740L;

    private boolean finalState;

    private boolean success;

    private String code;

    private String msg;

    public TransResponse() {
    }

    public TransResponse( TransResponse transResponse ) {
        this.setFinalState( transResponse.isFinalState() );
        this.setSuccess( transResponse.isSuccess() );
        this.setCode( transResponse.getCode() );
        this.setMsg( transResponse.getMsg() );
    }


    public TransResponse error( String code , String msg ) {

        this.setSuccess( false );
        this.setCode( code );
        this.setMsg( msg );

        return this;
    }

    public TransResponse error( ErrorInfo errorInfo ) {

        this.setSuccess( false );
        this.setCode( errorInfo.errorCode() );
        this.setMsg( errorInfo.errorMsg() );

        return this;
    }


    public boolean isFinalState() {
        return finalState;
    }

    public TransResponse setFinalState(boolean finalState) {
        this.finalState = finalState;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public TransResponse setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getCode() {
        return code;
    }

    public TransResponse setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public TransResponse setMsg(String msg) {
        this.msg = msg;
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

    public <R> R getResult() {
        return null;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
