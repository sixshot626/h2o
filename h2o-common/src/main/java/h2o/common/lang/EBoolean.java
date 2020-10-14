package h2o.common.lang;

public enum EBoolean implements NullableValue {

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


    public static EBoolean valueOf( boolean bool ) {
        return bool ? TRUE : FALSE;
    }

    public static EBoolean valueOf( Boolean bool ) {
        if ( bool == null ) {
            return NULL;
        } else {
            return bool.booleanValue() ? TRUE : FALSE;
        }
    }

    public static EBoolean valueOf( BBoolean bool ) {
       return EBoolean.valueOf( bool.getValue() );
    }


}
