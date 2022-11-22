package h2o.dao.result;

import h2o.common.collection.KeyMap;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class RowData extends KeyMap<Object> implements Map<String,Object>, Serializable {

    private static final long serialVersionUID = 7087116274734135207L;

    public RowData(Map<String, Object> map) {
        super(toLowerCaseKey(map));
    }

    private static Map<String, Object> toLowerCaseKey( Map<String, Object> map ) {
        Map<String,Object> data = new LinkedHashMap<>(map.size());
        for( Map.Entry<String,Object> entry : map.entrySet() ) {
            data.put( entry.getKey().toLowerCase() , entry.getValue() );
        }
        return data;
    }
}
