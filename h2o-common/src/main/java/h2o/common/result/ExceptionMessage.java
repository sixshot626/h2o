package h2o.common.result;

public class ExceptionMessage extends ErrorMessage implements java.io.Serializable {

    private static final long serialVersionUID = 3394450685156555678L;

    private final Throwable e;

    public ExceptionMessage(Throwable e) {
        super(e.getMessage());
        this.e = e;
    }

    public ExceptionMessage(Throwable e , String msg) {
        super(msg);
        this.e = e;
    }

    public ExceptionMessage(Throwable e , String code, String msg) {
        super(code, msg);
        this.e = e;
    }

    public Throwable getE() {
        return e;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExceptionMessage{");
        sb.append("code='").append(this.getCode()).append('\'');
        sb.append(", message='").append(this.getMessage()).append('\'');
        sb.append(", e=").append(e);
        sb.append('}');
        return sb.toString();
    }
}
