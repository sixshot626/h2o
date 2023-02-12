package h2o.common.thirdparty.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import h2o.common.exception.ExceptionUtil;
import h2o.common.json.JsonUtil;

import java.util.Map;

public class JacksonUtil implements JsonUtil {


    private final ObjectMapper objectMapper;

    public JacksonUtil() {
        this.objectMapper = new ObjectMapper();
    }

    public JacksonUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch ( Exception e ) {
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> fromJson(String json) {
        try {
            return objectMapper.readValue(json,Map.class);
        } catch ( Exception e ) {
            throw ExceptionUtil.toRuntimeException(e);
        }
    }
}
