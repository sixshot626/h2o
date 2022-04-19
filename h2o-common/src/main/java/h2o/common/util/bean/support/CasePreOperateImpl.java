package h2o.common.util.bean.support;

import h2o.common.util.bean.PreOperate;

public class CasePreOperateImpl extends AbstractPreOperate<String> implements PreOperate<String>, java.io.Serializable {

    private static final long serialVersionUID = -3454278658454673353L;

    public static final String LOWER = "LOWER";
    public static final String UPPER = "UPPER";

    public CasePreOperateImpl() {
        super();
    }

    public CasePreOperateImpl(PreOperate<String> ppreOp) {
        super(ppreOp);
    }

    private volatile boolean isLower = false;

    public void setToCase(String toCase) {
        this.isLower = "LOWER".equals(toCase.toUpperCase());
    }

    public CasePreOperateImpl(String toCase) {
        this.setToCase(toCase);
    }

    public CasePreOperateImpl(PreOperate<String> ppreOp, String toCase) {
        super(ppreOp);
        this.setToCase(toCase);
    }

    @Override
    protected String doOperateImpl(String o) {
        if (o != null) {
            if (isLower) {
                o = o.toLowerCase();
            } else {
                o = o.toUpperCase();
            }
        }
        return o;
    }

}
