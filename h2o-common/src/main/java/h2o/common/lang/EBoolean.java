package h2o.common.lang;

public enum EBoolean implements NullableValue {

    False,True,Null;


    @Override
    public boolean isPresent() {
        return this != Null;
    }

    public Boolean getValue() {
        return this == Null ? null : Boolean.valueOf( this == True );
    }

    public Boolean get() {

        if ( this.isPresent() ) {
            return Boolean.valueOf( this == True );
        } else {
            throw new IllegalStateException();
        }

    }


    public static EBoolean valueOf( boolean bool ) {
        return bool ? True : False;
    }

    public static EBoolean valueOf( Boolean bool ) {
        if ( bool == null ) return Null;
        return bool.booleanValue() ? True : False;
    }



}
