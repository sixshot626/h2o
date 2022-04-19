package h2o.common.util.bean.serialize;

/**
 * Created by zhangjianwei on 16/8/24.
 */
public interface BeanStrSerializer {

    String bean2string(Object bean);

    Object string2bean(String str);

}
