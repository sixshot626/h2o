package h2o.common.util.bean.serialize;


public interface BeanSerializer {

    byte[] bean2bytes(Object bean);

    Object bytes2bean(byte[] bs);
}
