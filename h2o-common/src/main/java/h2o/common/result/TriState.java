package h2o.common.result;

import h2o.common.lang.EBoolean;

public enum TriState {

    Success(0),Failure(9),Unknown(1);


    public final int value;

    TriState(int value) {
        this.value = value;
    }

    public static TriState valueOf( EBoolean ok ) {
        if ( ok == EBoolean.True ) {
            return Success;
        } else if ( ok == EBoolean.False ) {
            return Failure;
        } else {
            return Unknown;
        }
    }

}
