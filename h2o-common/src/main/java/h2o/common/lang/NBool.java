package h2o.common.lang;

public enum NBool implements NullableValue {

    FALSE, TRUE, NULL;


    @Override
    public boolean isPresent() {
        return this != NULL;
    }

    public Boolean getValue() {
        return this == NULL ? null : Boolean.valueOf( this == TRUE);
    }

    public boolean get() {

        if ( this.isPresent() ) {
            return this == TRUE;
        } else {
            throw new IllegalStateException();
        }

    }


    public static NBool valueOf(boolean bool ) {
        return bool ? TRUE : FALSE;
    }

    public static NBool valueOf(Boolean bool ) {
        if ( bool == null ) {
            return NULL;
        } else {
            return bool.booleanValue() ? TRUE : FALSE;
        }
    }

}
