package h2o.common.util.bean.support;

import h2o.common.util.bean.PreOperate;

public class TrimPreOperateImpl<T> extends AbstractPreOperate<T> implements PreOperate<T>, java.io.Serializable {


    private static final long serialVersionUID = -7324337525859382927L;

    public TrimPreOperateImpl() {
        super();
    }

    public TrimPreOperateImpl(PreOperate<T> ppreOp) {
        super(ppreOp);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T doOperateImpl(T o) {
        if (o != null && o instanceof String) {
            o = (T) ((String) o).trim();
        }
        return o;
    }

}
