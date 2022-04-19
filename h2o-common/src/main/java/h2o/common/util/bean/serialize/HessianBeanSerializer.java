package h2o.common.util.bean.serialize;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import h2o.common.exception.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by zhangjianwei on 2017/6/25.
 */
public class HessianBeanSerializer implements BeanSerializer {

    private static final Logger log = LoggerFactory.getLogger(HessianBeanSerializer.class.getName());


    @Override
    public byte[] bean2bytes(Object bean) {

        try {

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            HessianOutput ho = new HessianOutput(os);
            ho.writeObject(bean);
            return os.toByteArray();

        } catch (Exception e) {
            log.debug("", e);
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

    @Override
    public Object bytes2bean(byte[] bs) {

        try {

            ByteArrayInputStream is = new ByteArrayInputStream(bs);
            HessianInput hi = new HessianInput(is);
            return hi.readObject();

        } catch (Exception e) {
            log.debug("", e);
            throw ExceptionUtil.toRuntimeException(e);
        }

    }

}
