package h2o.flow.pvm.runtime;

import h2o.common.collections.builder.ListBuilder;
import h2o.flow.pvm.elements.Line;

import java.util.Collections;
import java.util.List;

public class ExecResult implements java.io.Serializable {

    private final RunStatus status;

    private final List<Line> lines;

    private Object result;

    public ExecResult(RunStatus status, Line line ) {
        this.status = status;
        this.lines = Collections.unmodifiableList(ListBuilder.newList(line));
    }

    public ExecResult(RunStatus status, List<Line> lines ) {
        this.status = status;
        this.lines = Collections.unmodifiableList(lines);
    }

    public RunStatus getStatus() {
        return status;
    }

    public List<Line> getLines() {
        return lines;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NodeExecResult{");
        sb.append("status=").append(status);
        sb.append(", lines=").append(lines);
        sb.append(", result=").append(result);
        sb.append('}');
        return sb.toString();
    }
}
