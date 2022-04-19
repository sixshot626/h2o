package h2o.common.util.bean.serialize;

import h2o.common.util.security.Base64Util;

/**
 * Created by zhangjianwei on 2017/6/24.
 */
public class Base64StrSerializeProxy implements BeanSerializer, BeanStrSerializer {

    private final BeanSerializer realBeanSerializer;

    private volatile Base64Util base64Util = new Base64Util();

    public Base64StrSerializeProxy(BeanSerializer beanSerializer) {
        this.realBeanSerializer = beanSerializer;
    }

    @Override
    public byte[] bean2bytes(Object bean) {
        return realBeanSerializer.bean2bytes(bean);
    }

    @Override
    public Object bytes2bean(byte[] bs) {
        return realBeanSerializer.bytes2bean(bs);
    }

    @Override
    public String bean2string(Object bean) {
        return base64Util.encode(this.bean2bytes(bean));
    }

    @Override
    public Object string2bean(String str) {
        return this.bytes2bean(base64Util.decode(str));
    }


    public void setBase64Util(Base64Util base64Util) {
        this.base64Util = base64Util;
    }
}
