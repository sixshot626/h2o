package h2o.common.util.bean.support;

import h2o.common.util.bean.PreOperate;

public class FilterNullOperateImpl<T> extends AbstractPreOperate<T> implements PreOperate<T>, java.io.Serializable {


    private static final long serialVersionUID = 3040606256193373723L;

    public FilterNullOperateImpl() {
        super();
    }

    public FilterNullOperateImpl(PreOperate<T> ppreOp) {
        super(ppreOp);
    }

    @Override
    protected T doOperateImpl(T o) {
        if (o == null || !(o instanceof String) || "".equals(((String) o).trim())) {
            return null;
        }
        return o;
    }

}
