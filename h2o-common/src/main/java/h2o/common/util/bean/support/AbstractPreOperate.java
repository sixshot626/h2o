package h2o.common.util.bean.support;

import h2o.common.util.bean.PreOperate;

public abstract class AbstractPreOperate<T> implements PreOperate<T> {

    private volatile PreOperate<T> ppreOp;

    public void setPpreOp(PreOperate<T> ppreOp) {
        this.ppreOp = ppreOp;
    }

    public AbstractPreOperate() {
    }

    public AbstractPreOperate(PreOperate<T> ppreOp) {
        this.setPpreOp(ppreOp);
    }

    public final T doOperate(T o) {

        if (this.ppreOp != null) {
            o = this.ppreOp.doOperate(o);
        }

        return this.doOperateImpl(o);

    }

    protected abstract T doOperateImpl(T o);

}
