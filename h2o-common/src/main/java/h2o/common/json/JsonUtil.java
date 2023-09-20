package h2o.common.json;

import java.util.List;
import java.util.Map;

public interface JsonUtil {

    String toJson( Object obj );

    Map<String,Object> fromJson( String json );

    <T> T fromJson(String json , Class<T> clazz);

}
