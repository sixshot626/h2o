package h2o.utils.log;

import h2o.common.exception.ExceptionUtil;
import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.date.DateUtil;
import h2o.common.util.format.FormattingTuple;
import h2o.common.util.format.MessageFormatter;

import java.util.Date;

public abstract class AbstractTagLogger implements  TagLogger {


    @Override
    public abstract void log(LogLevel level, String[] tags, String prompt, Object log);

    @Override
    public void fmtLog(LogLevel level, String[] tags, String prompt, String fmt, Object... args ) {

        FormattingTuple tp = MessageFormatter.arrayFormat( fmt , args);
        if ( tp.getThrowable() != null ) {
            this.log( level , tags , prompt , tp.getThrowable() );
        } else {
            this.log( level , tags , prompt , tp.getMessage() );
        }

    }

    protected String formatLog( LogLevel level , String[] tags , String prompt, Object log ) {



        StringBuilder sbm = new StringBuilder();
        sbm.append( DateUtil.toLongString( new Date()  ) );
        sbm.append( " [" );
        sbm.append( level );
        sbm.append( "] tags : {" );

        if ( !CollectionUtil.argsIsBlank( tags ) ) {
            for ( int i = 0 ; i < tags.length ; i++ ) {
                if ( i > 0 ) {
                    sbm.append( "," );
                }
                sbm.append( tags[i] );
            }
        }

        sbm.append( "} " );

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
