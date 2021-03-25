package h2o.common.lang;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public final class SNumber extends Number implements NullableValue, Comparable<SNumber> , java.io.Serializable {

    private static final long serialVersionUID = -2650821778406349289L;

    public static final SNumber NULL = new SNumber();
    public static final SNumber ZERO = new SNumber("0",true);
    public static final SNumber ONE  = new SNumber("1",true);


    private final String value;

    public SNumber() {
        this.value = null;
    }


    public SNumber( long value ) {
        this.value = BigDecimal.valueOf(value).toString();
    }

    public SNumber( double value ) {
        this.value = BigDecimal.valueOf(value).toString();
    }

    public SNumber( Number number ) {

        if ( number == null ) {
            this.value = null;
        } else if ( number instanceof Integer || number instanceof Double || number instanceof Long ||
                         number instanceof BigDecimal || number instanceof Float ||
                        number instanceof BigInteger || number instanceof Short  ) {
            this.value = number.toString();
        } else if ( number instanceof SNumber ) {
            this.value = ((SNumber)number).value;
        } else {
            this.value = new BigDecimal(number.toString().trim()).toString();
        }

    }

    public SNumber( String num ) {
       this( num , false );
    }

    public SNumber( String num , boolean direct ) {
       if ( direct ) {
           this.value = num;
       } else {
           this.value = num == null ? null : new BigDecimal(num).toString();
       }
    }

    public SNumber( SNumber snumber ) {
        this.value = snumber.value;
    }



    @Override
    public boolean isPresent() {
        return value != null;
    }

    public String getValue() {
        return value;
    }

    public String get() {

        if ( this.isPresent() ) {
            return value;
        }

        throw new IllegalStateException();

    }

    public String orElse(String other) {
        return this.isPresent() ? value : other;
    }


    public String fmt( String fmt ) {
       return new DecimalFormat(fmt).format( this.toBigDecimalExact() );
    }


    public String fmt( String fmt , String def ) {
        return this.isPresent() ? this.fmt( fmt ) : def;
    }



    public BigDecimal toBigDecimal() {
        if ( this.isPresent() ) {
            return new BigDecimal( this.value );
        } else {
            return null;
        }
    }

    public BigDecimal toBigDecimalExact() {
        if ( this.isPresent() ) {
            return new BigDecimal( this.value );
        } else {
            throw new IllegalStateException();
        }
    }


    public BigInteger toBigInteger() {
        if ( this.isPresent() ) {
            return new BigDecimal( this.value ).toBigInteger();
        } else {
            return null;
        }
    }

    public BigInteger toBigIntegerExact() {
        if ( this.isPresent() ) {
            return new BigDecimal( this.value ).toBigIntegerExact();
        } else {
            throw new IllegalStateException();
        }
    }


    public Integer toInteger() {
        if ( this.isPresent() ) {
            return Integer.valueOf(new BigDecimal( this.value ).intValue());
        } else {
            return null;
        }
    }

    public Integer toIntegerExact() {
        if ( this.isPresent() ) {
            return Integer.valueOf(new BigDecimal( this.value ).intValueExact());
        } else {
            throw new IllegalStateException();
        }
    }

    public Long toLong() {
        if ( this.isPresent() ) {
            return Long.valueOf(new BigDecimal( this.value ).longValue());
        } else {
            return null;
        }
    }

    public Long toLongExact() {
        if ( this.isPresent() ) {
            return Long.valueOf(new BigDecimal( this.value ).longValueExact());
        } else {
            throw new IllegalStateException();
        }
    }

    public Float toFloat() {
        if ( this.isPresent() ) {
            return Float.valueOf(new BigDecimal( this.value ).floatValue());
        } else {
            return null;
        }
    }

    public Float toFloatExact() {
        if ( this.isPresent() ) {
            return Float.valueOf(new BigDecimal( this.value ).floatValue());
        } else {
            throw new IllegalStateException();
        }
    }



    public Double toDouble() {
        if ( this.isPresent() ) {
            return Double.valueOf(new BigDecimal( this.value ).doubleValue());
        } else {
            return null;
        }
    }

    public Double toDoubleExact() {
        if ( this.isPresent() ) {
            return Double.valueOf(new BigDecimal( this.value ).doubleValue());
        } else {
            throw new IllegalStateException();
        }
    }

    public Boolean toBoolean() {
        if ( this.isPresent() ) {
            return !(new BigDecimal( this.value ).toBigInteger().compareTo( BigInteger.ZERO ) == 0 );
        } else {
            return null;
        }
    }

    public Boolean toBooleanExact() {
        if ( this.isPresent() ) {
            return !(new BigDecimal( this.value ).intValueExact() == 0);
        } else {
            throw new IllegalStateException();
        }
    }



    @Override
    public int intValue() {
        if ( this.isPresent() ) {
            return new BigDecimal( this.value ).intValue();
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public long longValue() {
        if ( this.isPresent() ) {
            return new BigDecimal( this.value ).longValue();
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public float floatValue() {
        if ( this.isPresent() ) {
            return new BigDecimal( this.value ).floatValue();
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public double doubleValue() {
        if ( this.isPresent() ) {
            return new BigDecimal( this.value ).doubleValue();
        } else {
            throw new IllegalStateException();
        }
    }



    public SNumber add(SNumber augend) {

        if ( augend == null || ( !augend.isPresent() ) ) {
            throw new IllegalArgumentException();
        }
        if ( !this.isPresent() ) {
            throw new IllegalStateException();
        }

        return new SNumber(new BigDecimal(this.value).add( new BigDecimal(augend.value) ) );
    }

    public SNumber subtract(SNumber subtrahend) {

        if ( subtrahend == null || ( !subtrahend.isPresent() ) ) {
            throw new IllegalArgumentException();
        }
        if ( !this.isPresent() ) {
            throw new IllegalStateException();
        }

        return new SNumber( new BigDecimal(this.value).subtract( new BigDecimal(subtrahend.value) ) );
    }

    public SNumber multiply(SNumber multiplicand) {

        if ( multiplicand == null || ( !multiplicand.isPresent() ) ) {
            throw new IllegalArgumentException();
        }
        if ( !this.isPresent() ) {
            throw new IllegalStateException();
        }

        return new SNumber( new BigDecimal(this.value).multiply( new BigDecimal(multiplicand.value) ) );
    }

    public SNumber divide(SNumber divisor) {

        if ( divisor == null || ( !divisor.isPresent() ) ) {
            throw new IllegalArgumentException();
        }
        if ( !this.isPresent() ) {
            throw new IllegalStateException();
        }

        return new SNumber( new BigDecimal(this.value).divide( new BigDecimal(divisor.value) ) );
    }

    public SNumber divide(SNumber divisor , RoundingMode roundingMode) {

        if ( divisor == null || ( !divisor.isPresent() ) ) {
            throw new IllegalArgumentException();
        }
        if ( !this.isPresent() ) {
            throw new IllegalStateException();
        }

        return new SNumber( new BigDecimal(this.value).divide( new BigDecimal(divisor.value) , roundingMode ) );
    }

    public SNumber divide(SNumber divisor, int scale, RoundingMode roundingMode) {

        if ( divisor == null || ( !divisor.isPresent() ) ) {
            throw new IllegalArgumentException();
        }
        if ( !this.isPresent() ) {
            throw new IllegalStateException();
        }

        return new SNumber( new BigDecimal(this.value).divide( new BigDecimal(divisor.value) , scale, roundingMode ) );
    }

    public SNumber toScale(int scale, RoundingMode roundingMode) {

        if ( !this.isPresent() ) {
            throw new IllegalStateException();
        }

        return new SNumber( new BigDecimal(this.value).setScale( scale , roundingMode ) );
    }




    @Override
    public int compareTo(SNumber o) {
        if ( this.isPresent() && o.isPresent() ) {
            return this.toBigDecimal().compareTo( o.toBigDecimal() );
        } else if ( ( ! this.isPresent() ) && ( ! o.isPresent())  ) {
            return 0;
        } else {
            return this.isPresent() ? 1 : -1;
        }
    }




    public boolean valueEquals( SNumber o ) {

       if ( o == null ) {
           return ! this.isPresent();
       }

       return this.compareTo( o ) == 0;

    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || !(o instanceof SNumber)) return false;

        SNumber sNumber = (SNumber) o;

        return new EqualsBuilder()
                .append(value, sNumber.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(value)
                .toHashCode();
    }


    @Override
    public String toString() {
        return this.orElse("<null>");
    }

}
