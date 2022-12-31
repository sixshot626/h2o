package h2o.common.util.bean.support;

import h2o.common.util.bean.PreOperate;

public abstract class AbstractPreOperate<T> implements PreOperate<T> {

    private volatile PreOperate<T> preOp;

    public void setPreOp(PreOperate<T> preOp) {
        this.preOp = preOp;
    }

    public AbstractPreOperate() {
    }

    public AbstractPreOperate(PreOperate<T> preOp) {
        this.setPreOp(preOp);
    }

    @Override
    public final T doOperate(T o) {

        if (this.preOp != null) {
            o = this.preOp.doOperate(o);
        }

        return this.doOperateImpl(o);

    }

    protected abstract T doOperateImpl(T o);

}
