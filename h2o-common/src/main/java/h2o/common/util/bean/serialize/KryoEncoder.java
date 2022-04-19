package h2o.common.util.bean.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import h2o.common.exception.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;

public class KryoEncoder implements BeanSerializer {

    private static final Logger log = LoggerFactory.getLogger(KryoEncoder.class.getName());

    public final Kryo kryo;

    public KryoEncoder() {
        this.kryo = new Kryo();
    }

    public KryoEncoder(Kryo kryo) {
        this.kryo = kryo;
    }

    public byte[] bean2bytes(Object bean) {

        Output out = null;
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            out = new Output(bao);
            kryo.writeClassAndObject(out, bean);

            return out.toBytes();

        } catch (Exception e) {
            log.debug("", e);
            throw ExceptionUtil.toRuntimeException(e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public Object bytes2bean(byte[] bs) {

        Input in = null;
        try {

            Object obj = null;

            in = new Input(bs);
            obj = kryo.readClassAndObject(in);

            return obj;

        } catch (Exception e) {
            log.debug("", e);
            throw ExceptionUtil.toRuntimeException(e);
        } finally {
            if (in != null) {
                in.close();
            }
        }

    }

}
