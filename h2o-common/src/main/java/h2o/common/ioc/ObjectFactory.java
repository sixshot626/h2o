package h2o.common.ioc;


import h2o.common.Mode;

public class ObjectFactory {

    private ObjectFactory() {
    }

    private static final ButterflyFactory bf = new ButterflyFactory("app", Mode.prodMode ? "app.bcs" : "app-" + Mode.config + ".bcs");


    public static <T> T get(String id, Object... args) {
        return bf.get(id, args);
    }


    public static <T> T silentlyGet(String id, Object... args) {
        return bf.silentlyGet(id, args);
    }


    public static void init() {
        bf.init();
    }

    public static void execPhase(String phase) {
        bf.execPhase(phase);
    }

    public static void execPhase(String phase, String name) {
        bf.execPhase(phase, name);
    }

    public static void dispose() {
        bf.dispose();
    }


}
