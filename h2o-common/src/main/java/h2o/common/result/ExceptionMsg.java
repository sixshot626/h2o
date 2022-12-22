package h2o.common.result;

public class ExceptionMsg extends ErrorMsg {

    private final Throwable e;

    public ExceptionMsg(Throwable e) {
        super(e.getMessage());
        this.e = e;
    }

    public ExceptionMsg(Throwable e , String msg) {
        super(msg);
        this.e = e;
    }

    public ExceptionMsg(Throwable e , String code, String msg) {
        super(code, msg);
        this.e = e;
    }

    public Throwable getE() {
        return e;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExceptionMsg{");
        sb.append("code='").append(this.getCode()).append('\'');
        sb.append(", msg='").append(this.getMsg()).append('\'');
        sb.append(", e=").append(e);
        sb.append('}');
        return sb.toString();
    }
}
