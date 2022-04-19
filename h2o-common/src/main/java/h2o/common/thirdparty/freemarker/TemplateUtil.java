package h2o.common.thirdparty.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import h2o.common.exception.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class TemplateUtil {

    private static final Logger log = LoggerFactory.getLogger(TemplateUtil.class.getName());


    @SuppressWarnings("deprecation")
    protected Configuration createDefaultConfiguration() {
        Configuration config = new Configuration();
        configurationPreSet(config);
        return config;
    }

    protected void configurationPreSet(Configuration config) {
    }


    public String process(Map<String, ?> data, String t) {

        java.io.StringReader sr = new java.io.StringReader(t);
        String s = this.process(data, sr);

        sr.close();

        return s;

    }


    public String process(Map<String, ?> data, Reader r) {

        StringWriter sw = new StringWriter();

        process(data, r, sw, this.createDefaultConfiguration());

        sw.flush();
        try {
            sw.close();
        } catch (IOException e) {
            log.debug("process", e);
        }

        return sw.toString();

    }


    public static String process(Map<String, ?> data, Template t) {

        StringWriter sw = new StringWriter();

        process(data, t, sw);

        sw.flush();
        try {
            sw.close();
        } catch (IOException e) {
            log.debug("process", e);
        }

        return sw.toString();

    }


    public static void process(Map<String, ?> data, Reader r, Writer w, Configuration c) {

        try {

            Template t = new Template("stringTemplate", r, c);
            t.process(data, w);


        } catch (Exception e) {
            log.debug("process", e);
            throw ExceptionUtil.toRuntimeException(e);
        }


    }

    public static void process(Map<String, ?> data, Template t, Writer w) {

        try {
            t.process(data, w);

        } catch (Exception e) {
            log.debug("process", e);
            throw ExceptionUtil.toRuntimeException(e);
        }


    }


}
