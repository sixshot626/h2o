package h2o.utils.log;

/**
 * Created by zhangjianwei on 16/8/18.
 */
public interface TagLogger {

    void log( LogLevel level,  String[] tags,  String prompt, Object log );

    void fmtLog( LogLevel level,  String[] tags,  String prompt,  String fmt , Object... args );

}
