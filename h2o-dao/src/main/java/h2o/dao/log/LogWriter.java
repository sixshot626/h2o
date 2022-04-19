package h2o.dao.log;

public interface LogWriter {

    default boolean isOn() {
        return true;
    }

    void write(String action, String sql, Object para);

}
