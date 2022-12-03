package h2o.utils.log;

/**
 * Created by zhangjianwei on 16/8/18.
 */
public interface TagLogger {

    boolean log( LogLevel level,  String[] tags,  String prompt, Object log );

    boolean fmtLog( LogLevel level,  String[] tags,  String prompt,  String fmt , Object... args );

}
