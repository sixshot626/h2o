package h2o.common.result;

public class ErrorMessage implements java.io.Serializable {

    private static final long serialVersionUID = 17412745159727608L;

    private final String code;

    private final String message;

    public ErrorMessage(String message) {
        this.code = null;
        this.message = message;
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
    public String toString() {
        final StringBuilder sb = new StringBuilder("ErrorMessage{");
        sb.append("code='").append(code).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
