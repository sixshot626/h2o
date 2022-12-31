package h2o.utils.log;


import h2o.apache.commons.lang.StringUtils;
import h2o.common.exception.ExceptionUtil;
import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.io.StreamUtil;
import h2o.common.util.lang.StringUtil;
import h2o.jodd.io.FileUtil;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.io.Writer;

/**
 * Created by zhangjianwei on 16/8/18.
 */
public class FileLogger extends AbstractTagLogger implements TagLogger , Serializable {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger( FileLogger.class.getName() );

    private final String basePath;

    private final LogMeta logMeta;

    private final boolean silent;

    public FileLogger(LogMeta logMeta , String basePath) {
        this.logMeta = logMeta;
        this.basePath = basePath;
        this.silent = true;
    }

    public FileLogger(LogMeta logMeta , String basePath , boolean silent ) {
        this.logMeta = logMeta;
        this.basePath = basePath;
        this.silent = silent;
    }


    FileLogger silent() {
        return new FileLogger( logMeta , basePath , true );
    }

    FileLogger notSilent() {
        return new FileLogger( logMeta , basePath , false );
    }



    @Override
    public boolean log( LogLevel level, String[] tags , String prompt, Object log ) {

        StringBuilder sb = new StringBuilder(basePath);
        if ( !basePath.endsWith("/") ) {
            sb.append('/');
        }

        if ( StringUtils.isNotBlank(logMeta.getModule()) ) {
            sb.append(logMeta.getModule());
            sb.append('/');
        }

        if ( !CollectionUtil.argsIsBlank( logMeta.getPath() ) ) {
            for (String p : logMeta.getPath()) {
                sb.append(p);
                sb.append('/');
            }
        }

        String path = sb.toString();
        boolean success = true;

        try {
            FileUtil.mkdirs(path);
        } catch (Exception e) {
            if ( !FileUtil.isExistingFolder(FileUtil.file(path))) {
                LOG.debug(StringUtil.EMPTY ,  e );
                if ( silent ) {
                    success = false;
                } else {
                    throw ExceptionUtil.toRuntimeException(e);
                }
            }
        }


        Writer w = null;
        try {

            w = StreamUtil.writeFile( FileUtil.file(path + logMeta.getId() + ".log" ), true );
            w.write( tagLogString( level , tags , prompt , log ) );
            w.write('\n');
            w.flush();

        } catch (Exception e) {

            LOG.error(StringUtil.EMPTY , e);
            if ( silent ) {
                success = false;
            } else {
                throw ExceptionUtil.toRuntimeException(e);
            }

        } finally {
            StreamUtil.close(w);
        }


        if ( !success ) {
            this.emergencyLog( path + logMeta.getId() + ".log" , tagLogString( level , tags , prompt , log )  );
        }

        return success;




    }

    private void emergencyLog( String file , String log  ) {
        try {

            LOG.error( "ORIGINAL -- ====================== {}:\n{}\n=======================================\n"
                    , file , log  );

            System.out.println( "EMERGENCY LOG -- ====================== "
                    + file + ":\n" + log + "\n=======================================\n" );

        } catch ( Exception e ) {
            LOG.error( StringUtil.EMPTY ,  e );
        }
    }



}
