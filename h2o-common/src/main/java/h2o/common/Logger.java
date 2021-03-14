package h2o.common;

import h2o.common.util.lang.RuntimeUtil;
import h2o.common.util.lang.StringUtil;
import org.slf4j.LoggerFactory;

public class Logger {
	
	private final String callClassName;
	
	public Logger() {
		this.callClassName = null;
	}
	
	public Logger(String callClassName ) {
		this.callClassName = callClassName;
	}


	private static final org.slf4j.Logger DEF_LOG = LoggerFactory.getLogger(Logger.class.getName());
	private static final String DEFFMT = "{}";


	private org.slf4j.Logger getLogger() {

		String logUtilClassName = callClassName == null ? Logger.class.getName() : callClassName;
        String loggerName = RuntimeUtil.getCallClassName(logUtilClassName);

		if (loggerName == null) {
			return DEF_LOG;
		} else {
            return LoggerFactory.getLogger(loggerName);
		}

	}

	public boolean isTraceEnabled() {
		return this.getLogger().isTraceEnabled();
	}

	public void trace(Object message) {
        this.getLogger().trace(DEFFMT, message);
	}

	public void trace(String fmt, Object... messages) {
        this.getLogger().trace(fmt, messages);
	}

	public void trace(String s, Throwable throwable) {
        this.getLogger().trace(s, throwable);
	}

    public void trace(Throwable throwable) {
        this.getLogger().trace(StringUtil.EMPTY, throwable);
    }

	public boolean isDebugEnabled() {
		return this.getLogger().isDebugEnabled();
	}

	public void debug(Object message) {
        this.getLogger().debug(DEFFMT, message);
	}

	public void debug(String fmt, Object... messages) {
        this.getLogger().debug(fmt, messages);
	}

	public void debug(String s, Throwable throwable) {
        this.getLogger().debug(s, throwable);
	}

    public void debug(Throwable throwable) {
        this.getLogger().debug(StringUtil.EMPTY, throwable);
    }

	public boolean isInfoEnabled() {
		return this.getLogger().isInfoEnabled();
	}

	public void info(Object message) {
        this.getLogger().info(DEFFMT, message);
	}

	public void info(String fmt, Object... messages) {
        this.getLogger().info(fmt, messages);
	}

	public void info(String s, Throwable throwable) {
        this.getLogger().info(s, throwable);
	}

    public void info(Throwable throwable) {
        this.getLogger().info(StringUtil.EMPTY, throwable);
    }

	public boolean isWarnEnabled() {
		return this.getLogger().isWarnEnabled();
	}

	public void warn(Object message) {
        this.getLogger().warn(DEFFMT, message);
	}

	public void warn(String fmt, Object... messages) {
        this.getLogger().warn(fmt, messages);
	}

	public void warn(String s, Throwable throwable) {
        this.getLogger().warn(s, throwable);
	}

    public void warn(Throwable throwable) {
        this.getLogger().warn(StringUtil.EMPTY, throwable);
    }

	public boolean isErrorEnabled() {
		return this.getLogger().isErrorEnabled();
	}

	public void error(Object message) {
        this.getLogger().error(DEFFMT, message);
	}

	public void error(String fmt, Object... messages) {
        this.getLogger().error(fmt, messages);
	}

	public void error(String s, Throwable throwable) {
        this.getLogger().error(s, throwable);
	}

    public void error(Throwable throwable) {
        this.getLogger().error(StringUtil.EMPTY, throwable);
    }

}
