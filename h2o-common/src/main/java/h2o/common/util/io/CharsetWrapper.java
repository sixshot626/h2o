package h2o.common.util.io;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class CharsetWrapper {

    public static final CharsetWrapper UNSET = new CharsetWrapper(null);

    public final String charset;

    public CharsetWrapper(String charset) {
        this.charset = charset;
    }

    public boolean isUnset() {
        return charset == null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CharsetWrapper that = (CharsetWrapper) o;

        return new EqualsBuilder()
                .append(charset, that.charset)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(charset)
                .toHashCode();
    }

}
