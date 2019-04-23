package h2o.common;

import h2o.common.util.bean.BeanUtil;
import h2o.common.util.json.GsonUtil;
import h2o.common.util.json.JsonUtil;


public class Tools {


	public static final BeanUtil b = BeanUtil.build().setProcNull(false).setIgnoreCase(false).setCover(false).get();

    public static final BeanUtil bn = b.procNull(true);

    public static final BeanUtil bi = b.ignoreCase(true);

    public static final BeanUtil bc = b.cover(true);

    public static final BeanUtil bni = bn.ignoreCase(true);

    public static final BeanUtil bnc = bn.cover(true);

    public static final BeanUtil bic = bi.cover(true);

    public static final BeanUtil bnic = bni.cover(true);


    public static class J {
        public static final JsonUtil json = new JsonUtil();
    }

    public static class G {
        public static final GsonUtil json = new GsonUtil();
    }


	public static final Logger log = new Logger();

}
