package h2o.utils.log;


import java.io.Serializable;

public class MultiLogger extends AbstractTagLogger implements TagLogger , Serializable {

    private final TagLogger[] loggers;

    public MultiLogger(TagLogger... loggers) {
        this.loggers = loggers;
    }

    @Override
    public void log(LogLevel level, String[] tags , String prompt, Object log) {

        for ( TagLogger logger : loggers  ) {
            logger.log( level , tags , prompt , log );
        }

    }


    public TagLogger[] getLoggers() {
        return loggers;
    }

}
