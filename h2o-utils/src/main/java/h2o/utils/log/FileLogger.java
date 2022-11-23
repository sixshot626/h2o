package h2o.utils.log;


import h2o.apache.commons.lang.StringUtils;
import h2o.common.util.io.StreamUtil;
import h2o.jodd.io.FileUtil;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

/**
 * Created by zhangjianwei on 16/8/18.
 */
public class FileLogger extends AbstractTagLogger implements TagLogger , Serializable {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger( FileLogger.class.getName() );

    private final String basePath;

    private final LogMeta logMeta;

    public FileLogger(LogMeta logMeta , String basePath) {
        this.logMeta = logMeta;
        this.basePath = basePath;
    }


    @Override
    public void log(LogLevel level, String[] tags , String prompt, Object log ) {

        StringBuilder sb = new StringBuilder(basePath);
        if ( !basePath.endsWith("/") ) {
            sb.append('/');
        }

        if (StringUtils.isNotBlank(logMeta.getModule())) {
            sb.append(logMeta.getModule());
            sb.append('/');
        }

        for( String p : logMeta.getPath() ) {
            sb.append(p);
            sb.append('/');
        }

        String path = sb.toString();

        Writer w = null;
        try {

            FileUtil.mkdirs(path);

            w = StreamUtil.writeFile( path + logMeta.getId() + ".log" , true );
            w.write( formatLog( level , tags , prompt , log ) );
            w.write('\n');
            w.flush();

        } catch (IOException e) {

            LOG.error("" , e);

        } finally {
            StreamUtil.close(w);
        }
    }


}
