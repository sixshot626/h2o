package h2o.dao.jdbc;

import h2o.common.lang.OptionalValue;

import java.sql.SQLType;
import java.util.Objects;

public final class TypedVal implements OptionalValue<Object> {

    private final Object value;

    private final SQLType sqlType;

    public TypedVal(Object value, SQLType sqlType) {
        this.value = value;
        this.sqlType = sqlType;
    }

    public Object getValue() {
        return value;
    }

    public SQLType getSqlType() {
        return sqlType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TypedVal typedVal = (TypedVal) o;
        return Objects.equals(value, typedVal.value) && Objects.equals(sqlType, typedVal.sqlType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, sqlType);
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TypedVal{");
        sb.append("value=").append(value);
        sb.append(", sqlType=").append(sqlType);
        sb.append('}');
        return sb.toString();
    }
}
