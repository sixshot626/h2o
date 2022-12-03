package h2o.utils.log;


import java.io.Serializable;

public class MultiLogger extends AbstractTagLogger implements TagLogger , Serializable {

    private final TagLogger[] loggers;

    public MultiLogger(TagLogger... loggers) {
        this.loggers = loggers;
    }

    @Override
    public boolean log(LogLevel level, String[] tags , String prompt, Object log) {

        boolean success = true;
        for ( TagLogger logger : loggers  ) {
            if ( ! logger.log( level , tags , prompt , log ) ) {
                success = false;
            }
        }

        return success;

    }


    public TagLogger[] getLoggers() {
        return loggers;
    }

}
