package h2o.dao.jdbc.row;

import h2o.common.collection.IgnoreCaseMap;
import h2o.common.lang.K;

import java.util.Map;

public class RowData extends IgnoreCaseMap<Object> {

    private static final long serialVersionUID = -3880014651131932729L;

    public RowData(Map m) {
        super(m, IgnoreCaseMap.LOWER);
    }

    @Override
    public Object get(Object obj) {

        if (obj == null) {
            throw new IllegalArgumentException();
        }

        String key;
        if (obj instanceof String) {
            key = (String) obj;
        } else if (obj instanceof K) {
            key = ((K) obj).getValue();
        } else if (obj instanceof Enum) {
            key = ((Enum) obj).name();
        } else {
            throw new IllegalArgumentException();
        }

        return super.get(key);
    }
}
