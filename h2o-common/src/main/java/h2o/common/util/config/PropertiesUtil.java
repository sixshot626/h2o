package h2o.common.util.config;

import h2o.common.exception.ExceptionUtil;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by zhangjianwei on 2016/10/14.
 */
public class PropertiesUtil {

    private final PropertiesConfiguration config;

    public PropertiesUtil(String propFile) {
        try {
            config = new PropertiesConfiguration(propFile);
        } catch (Throwable e) {
            e.printStackTrace();
            throw ExceptionUtil.toRuntimeException(e);
        }
    }


    protected void preSet(PropertiesConfiguration config) {
    }

    public Iterator<String> getKeys(String prefix) {
        return config.getKeys(prefix);
    }

    public Properties getProperties(String key) {
        return config.getProperties(key);
    }

    public Properties getProperties(String key, Properties defaults) {
        return config.getProperties(key, defaults);
    }

    public boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return config.getBoolean(key, defaultValue);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        return config.getBoolean(key, defaultValue);
    }

    public byte getByte(String key) {
        return config.getByte(key);
    }

    public byte getByte(String key, byte defaultValue) {
        return config.getByte(key, defaultValue);
    }

    public Byte getByte(String key, Byte defaultValue) {
        return config.getByte(key, defaultValue);
    }

    public double getDouble(String key) {
        return config.getDouble(key);
    }

    public double getDouble(String key, double defaultValue) {
        return config.getDouble(key, defaultValue);
    }

    public Double getDouble(String key, Double defaultValue) {
        return config.getDouble(key, defaultValue);
    }

    public float getFloat(String key) {
        return config.getFloat(key);
    }

    public float getFloat(String key, float defaultValue) {
        return config.getFloat(key, defaultValue);
    }

    public Float getFloat(String key, Float defaultValue) {
        return config.getFloat(key, defaultValue);
    }

    public int getInt(String key) {
        return config.getInt(key);
    }

    public int getInt(String key, int defaultValue) {
        return config.getInt(key, defaultValue);
    }

    public Integer getInteger(String key, Integer defaultValue) {
        return config.getInteger(key, defaultValue);
    }

    public long getLong(String key) {
        return config.getLong(key);
    }

    public long getLong(String key, long defaultValue) {
        return config.getLong(key, defaultValue);
    }

    public Long getLong(String key, Long defaultValue) {
        return config.getLong(key, defaultValue);
    }

    public short getShort(String key) {
        return config.getShort(key);
    }

    public short getShort(String key, short defaultValue) {
        return config.getShort(key, defaultValue);
    }

    public Short getShort(String key, Short defaultValue) {
        return config.getShort(key, defaultValue);
    }

    public BigDecimal getBigDecimal(String key) {
        return config.getBigDecimal(key);
    }

    public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        return config.getBigDecimal(key, defaultValue);
    }

    public BigInteger getBigInteger(String key) {
        return config.getBigInteger(key);
    }

    public BigInteger getBigInteger(String key, BigInteger defaultValue) {
        return config.getBigInteger(key, defaultValue);
    }

    public String getString(String key) {
        return config.getString(key);
    }

    public String getString(String key, String defaultValue) {
        return config.getString(key, defaultValue);
    }

    public String[] getStringArray(String key) {
        return config.getStringArray(key);
    }

    public List<Object> getList(String key) {
        return config.getList(key);
    }

    public List<Object> getList(String key, List<?> defaultValue) {
        return config.getList(key, defaultValue);
    }


    public Iterator<String> getKeys() {
        return config.getKeys();
    }

    public void setEncoding(String encoding) {
        config.setEncoding(encoding);
    }
}
