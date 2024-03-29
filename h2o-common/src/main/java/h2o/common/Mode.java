package h2o.common;

import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.io.StreamUtil;
import h2o.jodd.util.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

public class Mode {

    private static final Logger log = LoggerFactory.getLogger(Mode.class.getName());

    public static final String PROD = "PROD";
    public static final String TEST = "TEST";
    public static final String DEV = "DEV";

    public static final String mode;
    public static final String name;

    public static final String config;


    public static final boolean prodMode;
    public static final boolean testMode;
    public static final boolean devMode;

    public static final boolean debugMode;

    private static final String[] userModeArrays;

    static {

        boolean p = false;
        boolean t = false;
        boolean d = false;

        boolean debug = false;

        String m;
        String cfg;
        String userModes;

        try {

            final Properties config = new Properties();
            config.load(StreamUtil.openStream("mode.properties"));

            m = SystemUtil.get("H2OMode", config.getProperty("mode", PROD));

            cfg = SystemUtil.get("H2OConfig", config.getProperty("config", m)).toLowerCase();

            debug = SystemUtil.getBoolean("H2ODebug", Boolean.valueOf(config.getProperty("debug", "false").trim()));
            userModes = SystemUtil.get("H2OUserMode", config.getProperty("userMode", "")).trim().toUpperCase();

        } catch (Throwable e) {

            m = SystemUtil.get("H2OMode", PROD).trim().toUpperCase();

            cfg = SystemUtil.get("H2OConfig", m).trim().toLowerCase();

            debug = SystemUtil.getBoolean("H2ODebug", false);
            userModes = SystemUtil.get("H2OUserMode", "").trim().toUpperCase();

        }

        m = m.trim().toUpperCase();

        if (PROD.equals(m)) {
            p = true;
        } else if (TEST.equals(m)) {
            t = true;
        } else if (DEV.equals(m)) {
            d = true;
        } else {
            throw new IllegalArgumentException(m);
        }

        String[] uma = new String[0];

        if (!"".equals(userModes)) {
            List<String> ums = CollectionUtil.toList(userModes, new String[]{":", ",", ";", " ", "\t"});
            uma = ums.toArray(new String[ums.size()]);
        }

        prodMode = p;
        devMode = d;
        testMode = t;

        debugMode = debug;

        userModeArrays = uma;

        mode = m;
        name = m.toLowerCase();

        config = cfg;

        log.info("Mode : {}", mode);
        log.info("Config : {}", config);
        log.info("Debug Mode : {}", debugMode);
        log.info("User Mode : {}", userModeArrays);

    }

    private Mode() {
    }




    public static boolean isProdMode() {
        return prodMode;
    }

    public static boolean isTestMode() {
        return testMode;
    }

    public static boolean isDevMode() {
        return devMode;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }



    public static boolean isMode(String m) {

        if (m == null) {
            return false;
        }

        return m.trim().toUpperCase().equals(mode);
    }


    public static boolean isUserMode(String m) {

        if (m == null) {
            return false;
        }

        String um = m.trim().toUpperCase();

        for (String u : userModeArrays) {
            if (u.equals(um)) {
                return true;
            }
        }

        return false;
    }


}
