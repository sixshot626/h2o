package h2o.common;

import h2o.common.json.JsonUtil;
import h2o.common.thirdparty.json.JacksonUtil;
import h2o.common.util.bean.BeanUtil;


public class Tools {


    public static final BeanUtil b = BeanUtil.build().setProcNull(false).setIgnoreCase(false).setCover(false).get();

    public static final BeanUtil bn = b.procNull(true);

    public static final BeanUtil bi = b.ignoreCase(true);

    public static final BeanUtil bc = b.cover(true);

    public static final BeanUtil bni = bn.ignoreCase(true);

    public static final BeanUtil bnc = bn.cover(true);

    public static final BeanUtil bic = bi.cover(true);

    public static final BeanUtil bnic = bni.cover(true);


    public static final JsonUtil j = new JacksonUtil();


    public static final Logger log = new Logger();

}
