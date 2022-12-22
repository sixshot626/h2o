package h2o.common.result;

import h2o.common.lang.NBool;

public class FinalResult<R> implements java.io.Serializable {

    private final boolean finalState;
    private final R result;

    public FinalResult(boolean finalState, R result) {
        this.finalState = finalState;
        this.result = result;
    }

    public boolean isFinalState() {
        return finalState;
    }

    public R getResult() {
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FinalResult{");
        sb.append("finalState=").append(finalState);
        sb.append(", result=").append(result);
        sb.append('}');
        return sb.toString();
    }
}
