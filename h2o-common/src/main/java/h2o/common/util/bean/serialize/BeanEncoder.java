package h2o.common.util.bean.serialize;

import h2o.common.exception.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BeanEncoder implements BeanSerializer, BeanStrSerializer {

    private static final Logger log = LoggerFactory.getLogger(BeanEncoder.class.getName());

    private final String charsetName;

    public BeanEncoder() {
        this.charsetName = "UTF-8";
    }

    public BeanEncoder(String charsetName) {
        this.charsetName = charsetName;
    }

    public byte[] bean2bytes(Object bean) {

        try {

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bao);
            out.writeObject(bean);

            out.close();

            return bao.toByteArray();

        } catch (Exception e) {
            log.debug("", e);
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

    public Object bytes2bean(byte[] bs) {

        try {

            Object obj = null;
            ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(bs));

            try {
                obj = oin.readObject();
            } finally {
                if (oin != null) {
                    oin.close();
                }
            }

            return obj;

        } catch (Exception e) {
            log.debug("", e);
            throw ExceptionUtil.toRuntimeException(e);
        }

    }

    public String bean2string(Object bean) {

        try {

            ByteArrayOutputStream bao = new ByteArrayOutputStream();

            XMLEncoder xe = new XMLEncoder(bao);
            xe.writeObject(bean);
            xe.close();

            return new String(bao.toByteArray(), charsetName);

        } catch (Exception e) {
            log.debug("", e);
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

    public Object string2bean(String xml) {

        try {

            ByteArrayInputStream bai = new ByteArrayInputStream(xml.getBytes(charsetName));

            XMLDecoder xd = new XMLDecoder(bai);
            Object bean = xd.readObject();
            xd.close();

            return bean;

        } catch (Exception e) {
            log.debug("", e);
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

}
