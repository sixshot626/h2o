package h2o.dao.log;

public interface LogRecorder {

    void write( String prompt , String message );

}
