package h2o.common.result;

public class ErrorMsg implements java.io.Serializable {

    private final String code;

    private final String msg;

    public ErrorMsg(String msg) {
        this.code = null;
        this.msg = msg;
    }

    public ErrorMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ErrorMsg{");
        sb.append("code='").append(code).append('\'');
        sb.append(", msg='").append(msg).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
