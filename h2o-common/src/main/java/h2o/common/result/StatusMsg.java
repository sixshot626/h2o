package h2o.common.result;

public class StatusMsg<S> extends ErrorMsg {

    private final S status;

    public StatusMsg(S status , String msg) {
        super(msg);
        this.status = status;
    }

    public StatusMsg(S status , String code, String msg) {
        super(code, msg);
        this.status = status;
    }

    public S getStatus() {
        return status;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StatusMsg{");
        sb.append("status=").append(status);
        sb.append(", code='").append(this.getCode()).append('\'');
        sb.append(", msg='").append(this.getMsg()).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
