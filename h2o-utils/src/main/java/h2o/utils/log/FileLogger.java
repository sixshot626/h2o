package h2o.utils.log;


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

    private static final org.slf4j.Logger slfLog = LoggerFactory.getLogger( FileLogger.class.getName() );

    private final String baseDir;

    private final LogMeta logMeta;

    public FileLogger(LogMeta logMeta , String baseDir) {
        this.logMeta = logMeta;
        this.baseDir = baseDir;
    }


    @Override
    public void log(LogLevel level, String[] tags , String prompt, Object log ) {

        StringBuilder sb = new StringBuilder( baseDir );
        for( String p : logMeta.getPath() ) {
            sb.append("/");
            sb.append(p);
        }

        String path = sb.toString();

        Writer w = null;
        try {

            FileUtil.mkdirs(path);

            w = StreamUtil.writeFile( path + "/" + logMeta.getId() + ".txt" , true );
            w.write( formatLog( level , tags , prompt , log ) );
            w.flush();

        } catch (IOException e) {

            slfLog.error("" , e);

        } finally {
            StreamUtil.close(w);
        }
    }




}
