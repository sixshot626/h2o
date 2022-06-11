package h2o.flow.pvm.runtime;

public class NoDataFlowData implements FlowData , java.io.Serializable {

    private static final long serialVersionUID = -607037776041042620L;

    @Override
    public void marge(FlowData otherNodeData) {
    }

    @Override
    public FlowData copy() {
        return this;
    }
}
