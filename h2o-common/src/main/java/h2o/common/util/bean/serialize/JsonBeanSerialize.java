package h2o.common.util.bean.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import h2o.common.Tools;
import h2o.common.exception.ExceptionUtil;
import h2o.common.util.lang.InstanceUtil;
import h2o.common.util.lang.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * Created by zhangjianwei on 16/8/24.
 */
public class JsonBeanSerialize implements BeanStrSerialize , BeanSerialize  {

    private final FastJsonConfig fastJsonConfig = new FastJsonConfig();

    private final String charsetName;

    public JsonBeanSerialize() {
        this("UTF-8");
    }

    public JsonBeanSerialize( String charsetName ) {
        this.charsetName = charsetName;
    }


    protected void preSet( FastJsonConfig fastJsonConfig ) {}

    @Override
    public byte[] bean2bytes(Object bean) {

        try {

            return this.bean2string(bean).getBytes(charsetName);

        } catch (UnsupportedEncodingException e) {

            Tools.log.debug(e);
            throw ExceptionUtil.toRuntimeException(e);

        }

    }

    @Override
    public Object bytes2bean( byte[] bs ) {

        try {

            return this.string2bean( new String(bs,charsetName) );

        } catch (UnsupportedEncodingException e) {

            Tools.log.debug(e);
            throw ExceptionUtil.toRuntimeException(e);

        }

    }

    @Override
    public String bean2string(Object bean) {

        return StringUtil.append(
                new StringBuilder() ,
                bean.getClass().getName() , ":" , this.bean2json(bean)
        ).toString();
    }

    @Override
    public Object string2bean(String str) {

        String className = StringUtils.substringBefore(str,":");
        String body = StringUtils.substringAfter(str,":");

        return this.json2bean( body , InstanceUtil.getClass(className) );
    }

    protected String bean2json( Object bean ) {

       return JSON.toJSONString( bean ,
                fastJsonConfig.getSerializeConfig(),
                fastJsonConfig.getSerializeFilters(),
                fastJsonConfig.getDateFormat(),
                JSON.DEFAULT_GENERATE_FEATURE,
                fastJsonConfig.getSerializerFeatures()
       );
    }

    protected Object json2bean( String json , Class<?> c  ) {

        return JSON.parseObject( json, c , fastJsonConfig.getFeatures() );

    }

}