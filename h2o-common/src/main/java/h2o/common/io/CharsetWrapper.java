package h2o.common.io;


import java.util.Objects;

public class CharsetWrapper {

    public static final CharsetWrapper UNSET = new CharsetWrapper(null);

    public final String charset;

    public CharsetWrapper(String charset) {
        this.charset = charset;
    }

    public boolean isNotSet() {
        return charset == null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharsetWrapper that = (CharsetWrapper) o;
        return Objects.equals(charset, that.charset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(charset);
    }

    @Override
    public String toString() {
        return charset;
    }
}
