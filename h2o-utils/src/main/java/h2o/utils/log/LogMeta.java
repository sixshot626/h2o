package h2o.utils.log;

/**
 * Created by zhangjianwei on 16/8/18.
 */
public class LogMeta implements java.io.Serializable {

    private static final long serialVersionUID = -8068134767988144084L;

    private String module;

    private String[] path;

    private String id;


    public LogMeta() {
    }

    public LogMeta(String module, String[] path, String id) {
        this.module = module;
        this.path = path;
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String[] getPath() {
        return path;
    }

    public void setPath(String[] path) {
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
