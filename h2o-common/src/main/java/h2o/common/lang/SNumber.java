package h2o.common.lang;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class SNumber extends Number implements Comparable<SNumber> {

    private final String value;

    public SNumber() {
        this.value = null;
    }

    public SNumber( BigDecimal value ) {
        if ( value == null ) {
            this.value = null;
        } else {
            this.value = value.toString();
        }
    }

    public SNumber( Number number ) {
        if ( number == null ) {
            this.value = null;
        } else if ( number instanceof BigDecimal ) {
            this.value = number.toString();
        } else {
            this.value = new BigDecimal(number.toString()).toString();
        }
    }

    public SNumber(String num) {
        this.value = new BigDecimal(num).toString();
    }

    public boolean isPresent() {
        return value != null;
    }

    public String getValue() {
        return value;
    }

    public String get() {
        if ( this.value == null ) {
            throw new NullPointerException();
        }
        return value;
    }

    public String orElse(String other) {
        return value == null ? other : value;
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
        return new SNumber(new BigDecimal(value).add( new BigDecimal( augend.value) ));
    }

    public SNumber subtract(SNumber subtrahend) {
        return new SNumber( new BigDecimal(value).subtract( new BigDecimal(subtrahend.value) ));
    }

    public SNumber multiply(SNumber multiplicand) {
        return new SNumber( new BigDecimal(value).multiply( new BigDecimal( multiplicand.value) ));
    }

    public SNumber divide(SNumber divisor) {
        return new SNumber( new BigDecimal(value).divide( new BigDecimal(divisor.value) ));
    }

    public SNumber divide(SNumber divisor , RoundingMode roundingMode) {
        return new SNumber( new BigDecimal(value).divide( new BigDecimal(divisor.value) , roundingMode ) );
    }

    public SNumber divide(SNumber divisor, int scale, RoundingMode roundingMode) {
        return new SNumber( new BigDecimal(value).divide( new BigDecimal(divisor.value) , scale, roundingMode));
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
