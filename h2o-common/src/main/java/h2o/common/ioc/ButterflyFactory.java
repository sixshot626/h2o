package h2o.common.ioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ButterflyFactory implements Factory {

    private static final Logger log = LoggerFactory.getLogger(ButterflyFactory.class.getName());

    private final String name;

    private final Butterfly bf;

    public ButterflyFactory(String name, String bcs) {
        this.name = name;
        bf = new Butterfly(bcs);
    }


    @Override
    public <T> T get(String id, Object... args) {

        log.debug("{}: get({})", name, id);

        return bf.instance(id, args);
    }


    @Override
    public <T> T silentlyGet(String id, Object... args) {

        try {

            return get(id, args);

        } catch (Exception e) {
            log.debug("", e);
        }

        return null;
    }


    public void init() {
        bf.init();
    }

    public void execPhase(String phase) {
        bf.execPhase(phase);
    }

    public void execPhase(String phase, String name) {
        bf.execPhase(phase, name);
    }

    public void dispose() {
        bf.dispose();
    }

}
