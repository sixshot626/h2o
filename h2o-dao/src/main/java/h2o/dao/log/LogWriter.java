package h2o.dao.log;

public interface LogWriter {
    void write( String action , String sql , Object para );
}
