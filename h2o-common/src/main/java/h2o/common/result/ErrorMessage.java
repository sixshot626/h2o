package h2o.common.result;

public class ErrorMessage implements ErrorInfo, java.io.Serializable {

    private final String code;

    private final String message;

    public ErrorMessage(String msg) {
        this.code = null;
        this.message = msg;
    }

    public ErrorMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


    @Override
    public String errorCode() {
        return code;
    }

    @Override
    public String errorMsg() {
        return message;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ErrorMessage{");
        sb.append("code='").append(code).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
