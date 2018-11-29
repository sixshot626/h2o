package h2o.utils.log;


public class MultiLogger implements MetaLogger {

    private final MetaLogger[] loggers;

    public MultiLogger(MetaLogger... loggers) {
        this.loggers = loggers;
    }

    @Override
    public LogMeta getLogMeta() {
        return loggers[0].getLogMeta();
    }

    @Override
    public void log(LogLevel level, String prompt, Object log) {

        for ( MetaLogger logger : loggers  ) {
            logger.log( level , prompt , log );
        }

    }


    public MetaLogger[] getLoggers() {
        return loggers;
    }
}
