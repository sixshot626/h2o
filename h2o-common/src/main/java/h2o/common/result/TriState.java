package h2o.common.result;

import h2o.common.lang.NBool;
import h2o.jodd.util.StringUtil;

public enum TriState {

    SUCCESS(0), FAILURE(9), UNKNOWN(1);


    public final int value;

    TriState(int value) {
        this.value = value;
    }

    public static TriState of(NBool ok) {
        if (ok == NBool.TRUE) {
            return SUCCESS;
        } else if (ok == NBool.FALSE) {
            return FAILURE;
        } else {
            return UNKNOWN;
        }
    }

    public static TriState of( String val ) {
        if (StringUtil.isBlank( val )) {
            return UNKNOWN;
        }
        val = val.trim().toUpperCase();
        if ( "SUCCESS".equals( val) ) {
            return SUCCESS;
        } else if ( "FAILURE".equals( val ) ) {
            return FAILURE;
        } else {
            return UNKNOWN;
        }

    }

}
