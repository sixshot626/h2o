package h2o.common.result;

import h2o.common.lang.NBool;

public enum TriState {

    SUCCESS(0), FAILURE(9), UNKNOWN(1);


    public final int value;

    TriState(int value) {
        this.value = value;
    }

    public static TriState valueOf(NBool ok) {
        if (ok == NBool.TRUE) {
            return SUCCESS;
        } else if (ok == NBool.FALSE) {
            return FAILURE;
        } else {
            return UNKNOWN;
        }
    }

}
