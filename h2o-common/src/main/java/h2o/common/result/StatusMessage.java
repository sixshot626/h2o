package h2o.common.result;

public class StatusMessage<S> extends ErrorMessage implements java.io.Serializable {

    private static final long serialVersionUID = -7240739992619212868L;

    private final S status;

    public StatusMessage(S status , String msg) {
        super(msg);
        this.status = status;
    }

    public StatusMessage(S status , String code, String msg) {
        super(code, msg);
        this.status = status;
    }

    public S getStatus() {
        return status;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StatusMessage{");
        sb.append("status=").append(status);
        sb.append(", code='").append(this.getCode()).append('\'');
        sb.append(", message='").append(this.getMessage()).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
