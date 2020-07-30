package h2o.flow.pvm.runtime;

import h2o.flow.pvm.FlowException;

public class NoLineExcepion extends FlowException {

    public NoLineExcepion() {
    }

    public NoLineExcepion(String message, Throwable cause) {
        super(message, cause);
    }

    public NoLineExcepion(String message) {
        super(message);
    }

    public NoLineExcepion(Throwable cause) {
        super(cause);
    }

}
