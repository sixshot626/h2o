package h2o.common.result;

import h2o.common.lang.EBoolean;

public enum TriState {

    SUCCESS(0), FAILURE(9), UNKNOWN(1);


    public final int value;

    TriState(int value) {
        this.value = value;
    }

    public static TriState valueOf( EBoolean ok ) {
        if ( ok == EBoolean.TRUE) {
            return SUCCESS;
        } else if ( ok == EBoolean.FALSE) {
            return FAILURE;
        } else {
            return UNKNOWN;
        }
    }

}
