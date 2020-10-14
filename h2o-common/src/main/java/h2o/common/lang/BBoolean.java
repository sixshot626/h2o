package h2o.common.lang;

import java.util.Objects;

public class BBoolean implements NullableValue , java.io.Serializable {

    private static final long serialVersionUID = 294141628234416830L;

    public static final BBoolean TRUE   = new BBoolean(Boolean.TRUE);
    public static final BBoolean FALSE  = new BBoolean(Boolean.FALSE);
    public static final BBoolean NULL   = new BBoolean();



    protected final Boolean value;

    public BBoolean() {
        this.value = null;
    }

    public BBoolean(boolean value) {
        this.value = Boolean.valueOf(value);
    }

    public BBoolean(Boolean value) {
        if ( value == null ) {
            this.value = null;
        } else {
            this.value = Boolean.valueOf(value.booleanValue());
        }
    }

    public BBoolean( String value ) {
        if ( value == null ) {
            this.value = null;
        } else {
            this.value = Boolean.valueOf(value);
        }
    }

    public BBoolean( SNumber value ) {
        this.value = value.toBoolean();
    }

    public BBoolean( EBoolean value ) {
        if ( value == EBoolean.NULL) {
            this.value = null;
        } else {
            this.value = Boolean.valueOf( value == EBoolean.TRUE);
        }
    }

    public BBoolean( BBoolean bool ) {
        this.value = bool.value;
    }


    @Override
    public boolean isPresent() {
        return value != null;
    }

    public Boolean getValue() {
        return value;
    }

    public boolean get() {

        if ( this.isPresent() ) {
            return value.booleanValue();
        } else {
            throw new IllegalStateException();
        }

    }

    public Boolean orElse( Boolean other ) {
        return this.isPresent() ? value : other;
    }

    public EBoolean toEBoolean() {
        return EBoolean.valueOf( this.value );
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof BBoolean)) return false;
        BBoolean bBoolean = (BBoolean) o;
        return Objects.equals(value, bBoolean.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {

        if ( this.isPresent() ) {
            return value.toString();
        } else {
            return "<null>";
        }

    }




}
