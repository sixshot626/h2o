package h2o.utils.log;

import h2o.common.exception.ExceptionUtil;
import h2o.common.lang.LTime;
import h2o.common.lang.SDate;
import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.date.DateUtil;
import h2o.common.util.format.FormattingTuple;
import h2o.common.util.format.MessageFormatter;

import java.util.Date;

public abstract class AbstractTagLogger implements  TagLogger {



    @Override
    public boolean fmtLog(LogLevel level, String[] tags, String prompt, String fmt, Object... args ) {

        FormattingTuple tp = MessageFormatter.arrayFormat( fmt , args);
        if ( tp.getThrowable() != null ) {
            return this.log( level , tags , prompt , tp.getThrowable() );
        } else {
            return this.log( level , tags , prompt , tp.getMessage() );
        }

    }

    protected String tagLogString( LTime time , LogLevel level , String[] tags , String prompt, Object log ) {

        StringBuilder sbm = new StringBuilder();
        sbm.append( time.fmt( "yyyy-MM-dd HH:mm:ss.SSS" ,  DateUtil.toLongString( new Date()  ) ) );
        sbm.append( " " );
        sbm.append( level );
        sbm.append( " --- " );

        sbm.append( tagLogString( tags , prompt , log ) );

        return sbm.toString();


    }

    protected String tagLogString( String[] tags , String prompt, Object log ) {
        StringBuilder sbm = new StringBuilder();
        sbm.append( "[" );

        if ( !CollectionUtil.argsIsBlank( tags ) ) {
            for ( int i = 0 ; i < tags.length ; i++ ) {
                if ( i > 0 ) {
                    sbm.append( "," );
                }
                sbm.append( tags[i] );
            }
        }

        sbm.append( "] " );

        sbm.append(prompt);
        sbm.append(" ");

        if( log != null ) {

            sbm.append(  log instanceof Throwable ?
                    this.exceptionToString( (Throwable)log ) :
                    log.toString() );

        }

        return sbm.toString();

    }

    protected String exceptionToString( Throwable e ) {
        return ExceptionUtil.exceptionChainToString(e);
    }


}
