package h2o.flow.pvm.runtime;

import h2o.common.collections.builder.ListBuilder;
import h2o.flow.pvm.elements.Line;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ExecResult implements java.io.Serializable {

    private final RunStatus status;

    private final List<Line> lines;

    private boolean present;
    private Object result;



    public ExecResult(RunStatus status) {
        this.status = status;
        this.lines = Collections.emptyList();
    }

    public ExecResult(RunStatus status, Line... lines ) {
        this.status = status;
        this.lines = Collections.unmodifiableList(ListBuilder.newList(lines));
    }

    public ExecResult(RunStatus status, Collection<Line> lines ) {
        this.status = status;
        this.lines = Collections.unmodifiableList(ListBuilder.newListAndAddAll( lines ) );
    }


    public static ExecResult pause() {
        return new ExecResult( RunStatus.PAUSE );
    }

    public static ExecResult end() {
        return new ExecResult( RunStatus.END );
    }

    public static ExecResult exception() {
        return new ExecResult( RunStatus.EXCEPTION );
    }

    public static ExecResult goOn( Line... lines ) {
        return new ExecResult( RunStatus.RUNNING , lines );
    }

    public static ExecResult goOn( Collection<Line> lines ) {
        return new ExecResult( RunStatus.RUNNING , lines );
    }


    public RunStatus getStatus() {
        return status;
    }

    public List<Line> getLines() {
        return lines;
    }


    public boolean isPresent() {
        return present;
    }

    public Object getResult() {
        return result;
    }

    public ExecResult setResult(Object result) {
        this.result = result;
        this.present = true;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExecResult{");
        sb.append("status=").append(status);
        sb.append(", lines=").append(lines);
        sb.append(", result=").append(result);
        sb.append('}');
        return sb.toString();
    }

}
