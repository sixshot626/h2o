package h2o.common.bean.result;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ResultData implements ErrorInfo , java.io.Serializable {

    private static final long serialVersionUID = 26132204178801391L;

    private boolean success;

    private String errorField;

    private String errorCode;

    private String msg;

    private Object info;

    private Object result;

    private ResultData() {}


    public static ResultData success() {
        return new ResultData().setSuccess(true);
    }

    public static ResultData success(Object result ) {
        return ResultData.success().setResult(result);
    }

    public static ResultData error(String msg ) {
        return new ResultData().setSuccess(false).setMsg(msg);
    }

    public static ResultData error(String errorCode , String msg ) {
        return ResultData.error(msg).setErrorCode(errorCode);
    }


    public boolean isSuccess() {
        return success;
    }

    public ResultData setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getErrorField() {
        return errorField;
    }

    public ResultData setErrorField(String errorField) {
        this.errorField = errorField;
        return this;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public ResultData setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResultData setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }

    public Object getResult() {
        return result;
    }

    public ResultData setResult(Object result) {
        this.result = result;
        return this;
    }

    @Override
    public String errorCode() {
        return errorCode;
    }

    @Override
    public String errorMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
