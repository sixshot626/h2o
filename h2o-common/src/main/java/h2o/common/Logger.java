package h2o.common;

import h2o.common.util.lang.RuntimeUtil;
import h2o.common.util.lang.StringUtil;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Logger {

    private final String callClassName;

    public Logger() {
        this.callClassName = null;
    }

    public Logger(String callClassName) {
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


    public void _trace(List args) {
        if (this.isTraceEnabled() && args != null) {
            int n = args.size();
            if (n > 0) {
                if (n == 1) {
                    Object a = args.get(0);
                    if (a instanceof Throwable) {
                        this.trace((Throwable) a);
                    } else {
                        this.trace(a);
                    }
                } else if (n == 2) {
                    Object a0 = args.get(0);
                    Object a1 = args.get(1);
                    if (a1 instanceof Throwable) {
                        this.trace((String) a0, (Throwable) a1);
                    } else {
                        this.trace((String) a0, new Object[]{a1});
                    }
                } else {
                    Object[] a = args.toArray();
                    Object[] a2 = new Object[n - 1];
                    System.arraycopy(a, 1, a2, 0, n - 1);
                    this.trace((String) a[0], a2);
                }
            }
        }
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


    public void _debug(List args) {
        if (this.isDebugEnabled() && args != null) {
            int n = args.size();
            if (n > 0) {
                if (n == 1) {
                    Object a = args.get(0);
                    if (a instanceof Throwable) {
                        this.debug((Throwable) a);
                    } else {
                        this.debug(a);
                    }
                } else if (n == 2) {
                    Object a0 = args.get(0);
                    Object a1 = args.get(1);
                    if (a1 instanceof Throwable) {
                        this.debug((String) a0, (Throwable) a1);
                    } else {
                        this.debug((String) a0, new Object[]{a1});
                    }
                } else {
                    Object[] a = args.toArray();
                    Object[] a2 = new Object[n - 1];
                    System.arraycopy(a, 1, a2, 0, n - 1);
                    this.debug((String) a[0], a2);
                }
            }
        }
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

    public void _info(List args) {
        if (this.isInfoEnabled() && args != null) {
            int n = args.size();
            if (n > 0) {
                if (n == 1) {
                    Object a = args.get(0);
                    if (a instanceof Throwable) {
                        this.info((Throwable) a);
                    } else {
                        this.info(a);
                    }
                } else if (n == 2) {
                    Object a0 = args.get(0);
                    Object a1 = args.get(1);
                    if (a1 instanceof Throwable) {
                        this.info((String) a0, (Throwable) a1);
                    } else {
                        this.info((String) a0, new Object[]{a1});
                    }
                } else {
                    Object[] a = args.toArray();
                    Object[] a2 = new Object[n - 1];
                    System.arraycopy(a, 1, a2, 0, n - 1);
                    this.info((String) a[0], a2);
                }
            }
        }
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


    public void _warn(List args) {
        if (this.isWarnEnabled() && args != null) {
            int n = args.size();
            if (n > 0) {
                if (n == 1) {
                    Object a = args.get(0);
                    if (a instanceof Throwable) {
                        this.warn((Throwable) a);
                    } else {
                        this.warn(a);
                    }
                } else if (n == 2) {
                    Object a0 = args.get(0);
                    Object a1 = args.get(1);
                    if (a1 instanceof Throwable) {
                        this.warn((String) a0, (Throwable) a1);
                    } else {
                        this.warn((String) a0, new Object[]{a1});
                    }
                } else {
                    Object[] a = args.toArray();
                    Object[] a2 = new Object[n - 1];
                    System.arraycopy(a, 1, a2, 0, n - 1);
                    this.warn((String) a[0], a2);
                }
            }
        }
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


    public void _error(List args) {
        if (this.isErrorEnabled() && args != null) {
            int n = args.size();
            if (n > 0) {
                if (n == 1) {
                    Object a = args.get(0);
                    if (a instanceof Throwable) {
                        this.error((Throwable) a);
                    } else {
                        this.error(a);
                    }
                } else if (n == 2) {
                    Object a0 = args.get(0);
                    Object a1 = args.get(1);
                    if (a1 instanceof Throwable) {
                        this.error((String) a0, (Throwable) a1);
                    } else {
                        this.error((String) a0, new Object[]{a1});
                    }
                } else {
                    Object[] a = args.toArray();
                    Object[] a2 = new Object[n - 1];
                    System.arraycopy(a, 1, a2, 0, n - 1);
                    this.error((String) a[0], a2);
                }
            }
        }
    }

}
