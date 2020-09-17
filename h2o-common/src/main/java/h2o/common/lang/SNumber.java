package h2o.common.lang;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class SNumber extends Number implements Comparable<SNumber> {

    public static final SNumber ZERO = new SNumber("0",true);

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
            this.value = BigDecimal.valueOf(number.doubleValue()).toString();
        }

    }

    public SNumber(String num) {
       this( num , false );
    }

    public SNumber( String num , boolean direct ) {
       if ( direct ) {
           this.value = num;
       } else {
           this.value = new BigDecimal(num).toString();
       }
    }

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
        return value == null ? other : value;
    }


    public String fmt( String fmt ) {
       return new DecimalFormat(fmt).format( this.bigDecimalValue() );
    }


    public String fmt( String fmt , String def ) {
        return this.isPresent() ? this.fmt( fmt ) : def;
    }



    public BigDecimal bigDecimalValue() {
        return new BigDecimal( this.get() );
    }

    public BigInteger bigIntegerValue() {
        return new BigDecimal( this.get() ).toBigInteger();
    }



    @Override
    public int intValue() {
        return this.bigDecimalValue().intValue();
    }

    @Override
    public long longValue() {
        return this.bigDecimalValue().longValue();
    }

    @Override
    public float floatValue() {
        return this.bigDecimalValue().floatValue();
    }

    @Override
    public double doubleValue() {
        return this.bigDecimalValue().doubleValue();
    }



    public SNumber add(SNumber augend) {

        if ( augend == null || ( !augend.isPresent() ) ) {
            throw new IllegalArgumentException();
        }
        if ( !this.isPresent() ) {
            throw new IllegalStateException();
        }

        return new SNumber(new BigDecimal(value).add( new BigDecimal(augend.value) ) );
    }

    public SNumber subtract(SNumber subtrahend) {

        if ( subtrahend == null || ( !subtrahend.isPresent() ) ) {
            throw new IllegalArgumentException();
        }
        if ( !this.isPresent() ) {
            throw new IllegalStateException();
        }

        return new SNumber( new BigDecimal(value).subtract( new BigDecimal(subtrahend.value) ) );
    }

    public SNumber multiply(SNumber multiplicand) {

        if ( multiplicand == null || ( !multiplicand.isPresent() ) ) {
            throw new IllegalArgumentException();
        }
        if ( !this.isPresent() ) {
            throw new IllegalStateException();
        }

        return new SNumber( new BigDecimal(value).multiply( new BigDecimal(multiplicand.value) ) );
    }

    public SNumber divide(SNumber divisor) {

        if ( divisor == null || ( !divisor.isPresent() ) ) {
            throw new IllegalArgumentException();
        }
        if ( !this.isPresent() ) {
            throw new IllegalStateException();
        }

        return new SNumber( new BigDecimal(value).divide( new BigDecimal(divisor.value) ) );
    }

    public SNumber divide(SNumber divisor , RoundingMode roundingMode) {

        if ( divisor == null || ( !divisor.isPresent() ) ) {
            throw new IllegalArgumentException();
        }
        if ( !this.isPresent() ) {
            throw new IllegalStateException();
        }

        return new SNumber( new BigDecimal(value).divide( new BigDecimal(divisor.value) , roundingMode ) );
    }

    public SNumber divide(SNumber divisor, int scale, RoundingMode roundingMode) {

        if ( divisor == null || ( !divisor.isPresent() ) ) {
            throw new IllegalArgumentException();
        }
        if ( !this.isPresent() ) {
            throw new IllegalStateException();
        }

        return new SNumber( new BigDecimal(value).divide( new BigDecimal(divisor.value) , scale, roundingMode ) );
    }

    public SNumber toScale(int scale, RoundingMode roundingMode) {

        if ( !this.isPresent() ) {
            throw new IllegalStateException();
        }

        return new SNumber( new BigDecimal(value).setScale( scale , roundingMode ) );
    }




    @Override
    public int compareTo(SNumber o) {
        if ( this.isPresent() && o.isPresent() ) {
            return this.bigDecimalValue().compareTo( o.bigDecimalValue() );
        } else if ( ( ! this.isPresent() )&&  ( ! o.isPresent())  ) {
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

        if (o == null || getClass() != o.getClass()) return false;

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
