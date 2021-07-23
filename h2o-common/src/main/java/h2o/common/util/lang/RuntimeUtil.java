package h2o.common.util.lang;

public abstract class RuntimeUtil {

	private RuntimeUtil() {}


    public static String getCallClassName( String runClassName ) {

        String[] classNames = getCallingClasses();
        if ( classNames == null ) {
            return null;
        }

        String callClassName = null;

        int i = 0;
        for ( String cn : classNames) {

            if ( cn.equals(runClassName)) {
                i++;
            } else if (i > 0) {
                callClassName = cn;
                break;
            }

        }

        return callClassName;
    }




    public static String[] getCallingClasses() {

	    String[] classNames =  getCallingClassesBySecurityManager();
	    return classNames == null ? getCallingClassesByStackTrace() : classNames;

    }

    private static String[] getCallingClassesByStackTrace( ) {

        StackTraceElement[] stes = Thread.currentThread().getStackTrace();
        if ( stes == null ) {
            return null;
        }

        String[] classNames = new String[ stes.length ];
        for ( int i = 0 ; i < stes.length ; i++ ) {
            classNames[i] = stes[i].getClassName();
        }

        return classNames;
    }


    /**
     * In order to call {@link SecurityManager#getClassContext()}, which is a
     * protected method, we add this wrapper which allows the method to be visible
     * inside this package.
     */
    private static final class ClassContextSecurityManager extends SecurityManager {
        protected Class<?>[] getClassContext() {
            return super.getClassContext();
        }
    }

    private static RuntimeUtil.ClassContextSecurityManager SECURITY_MANAGER;
    private static boolean SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = false;
    static {
        getSecurityManager();
    }


    private static RuntimeUtil.ClassContextSecurityManager getSecurityManager() {
        if (SECURITY_MANAGER != null)
            return SECURITY_MANAGER;
        else if (SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED)
            return null;
        else {
            SECURITY_MANAGER = safeCreateSecurityManager();
            SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = true;
            return SECURITY_MANAGER;
        }
    }

    private static RuntimeUtil.ClassContextSecurityManager safeCreateSecurityManager() {
        try {
            return new RuntimeUtil.ClassContextSecurityManager();
        } catch (java.lang.SecurityException sm) {
            return null;
        }
    }


    private static String[] getCallingClassesBySecurityManager() {
        RuntimeUtil.ClassContextSecurityManager securityManager = getSecurityManager();
        if (securityManager == null) {
            return null;
        }

        Class<?>[] callingClasses = securityManager.getClassContext();
        if ( callingClasses == null ) {
            return null;
        }

        String[] classNames = new String[ callingClasses.length ];
        for ( int i = 0 ; i < callingClasses.length ; i++ ) {
            classNames[i] = callingClasses[i].getName();
        }

        return classNames;

    }




}
