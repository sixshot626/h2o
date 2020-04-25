package h2o.common.lang;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;

public class SNumber extends Number {

    private final String num;

    public SNumber() {
        this.num = null;
    }

    public SNumber(String num) {
        this.num = new BigDecimal(num).toString();
    }

    public boolean isPresent() {
        return num != null;
    }

    public String getValue() {
        return num;
    }

    public String get() {
        if ( this.num == null ) {
            throw new NullPointerException();
        }
        return num;
    }

    public String orElse(String other) {
        return num == null ? other : num;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SNumber sNumber = (SNumber) o;

        return new EqualsBuilder()
                .append(num, sNumber.num)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(num)
                .toHashCode();
    }


    @Override
    public String toString() {
        return this.orElse("<null>");
    }
}
