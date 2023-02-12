package h2o.common.json;

import java.util.Map;

public interface JsonUtil {

    String toJson( Object obj );

    Map<String,Object> fromJson( String json );

}
