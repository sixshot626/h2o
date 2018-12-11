package h2o.common.thirdparty.redis;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Created by zhangjianwei on 2017/7/3.
 */
public class RedisConfig {

    public final String host;

    public final int port;

    public final String pass;

    public final Integer timeout;

    public final Integer db;

    public RedisConfig(String host, int port) {
        this.host = host;
        this.port = port;
        this.pass = null;
        this.timeout = null;
        this.db = null;
    }

    public RedisConfig(String host, int port, String pass) {
        this.host = host;
        this.port = port;
        this.pass = pass;
        this.timeout = null;
        this.db = null;
    }

    public RedisConfig(String host, int port, String pass , Integer db ) {
        this.host = host;
        this.port = port;
        this.pass = pass;
        this.timeout = null;
        this.db = db;
    }

    public RedisConfig( String host, int port, Integer timeout , String pass, Integer db ) {
        this.host = host;
        this.port = port;
        this.pass = pass;
        this.timeout = timeout;
        this.db = db;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RedisConfig that = (RedisConfig) o;

        return new EqualsBuilder()
                .append(port, that.port)
                .append(host, that.host)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(host)
                .append(port)
                .toHashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RedisConfig{");
        sb.append("host='").append(host).append('\'');
        sb.append(", port=").append(port);
        sb.append(", db=").append(db);
        sb.append('}');
        return sb.toString();
    }
}
