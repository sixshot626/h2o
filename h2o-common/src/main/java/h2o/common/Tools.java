package h2o.common;

import h2o.common.ioc.ObjectFactory;
import h2o.common.json.JsonUtil;
import h2o.common.thirdparty.json.JacksonUtil;
import h2o.common.util.bean.BeanUtil;

import java.util.Map;


public class Tools {


    public static final BeanUtil b = BeanUtil.build().setProcNull(false).setIgnoreCase(false).setCover(false).get();

    public static final BeanUtil bn = b.procNull(true);

    public static final BeanUtil bi = b.ignoreCase(true);

    public static final BeanUtil bc = b.cover(true);

    public static final BeanUtil bni = bn.ignoreCase(true);

    public static final BeanUtil bnc = bn.cover(true);

    public static final BeanUtil bic = bi.cover(true);

    public static final BeanUtil bnic = bni.cover(true);


    public static final class j {

        private j() {}

        private static final JsonUtil JSON_UTIL = initJsonUtil();

        private static JsonUtil initJsonUtil() {

            JsonUtil jsonUtil = null;
            try {
                jsonUtil = ObjectFactory.silentlyGet("H2OJsonUtilProvider");
            } catch ( Exception e ) {}

            if ( jsonUtil == null ) {
                jsonUtil = new JacksonUtil();
            }

            log.info("JsonUtil impl: {}" , jsonUtil.getClass().getName());

            return jsonUtil;
        }

        public static String toJson(Object obj) {
            return JSON_UTIL.toJson(obj);
        }

        public static Map<String, Object> fromJson(String json) {
            return JSON_UTIL.fromJson(json);
        }

        public static <T> T fromJson(String json, Class<T> clazz) {
            return JSON_UTIL.fromJson(json, clazz);
        }
    }


    public static final Logger log = new Logger();

}
