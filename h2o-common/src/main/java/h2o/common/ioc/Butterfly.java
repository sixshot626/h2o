package h2o.common.ioc;

import com.jenkov.container.Container;
import com.jenkov.container.IContainer;
import com.jenkov.container.script.ScriptFactoryBuilder;
import h2o.common.util.io.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.io.StringReader;
import java.util.concurrent.locks.Lock;

public class Butterfly {

    private static final Logger log = LoggerFactory.getLogger(Butterfly.class.getName());

    private final Lock lock = new java.util.concurrent.locks.ReentrantLock();


    private volatile String path = "butterfly.bcs";
    private volatile String characterEncoding = null;

    private IContainer container;
    private ScriptFactoryBuilder scriptBuilder;

    private volatile boolean isInit = false;
    private volatile boolean isLoad = true;

    public Butterfly() {
    }

    ;

    public Butterfly(boolean isLoad) {
        this.isLoad = isLoad;
    }

    ;

    public Butterfly(String path) {
        this.setBCSPath(path);
    }

    ;


    public Butterfly setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
        return this;
    }

    public void build() {
        this.build(this.isLoad);
    }

    public void build(boolean isLoad) {

        lock.lock();
        try {

            container = new Container();
            scriptBuilder = new ScriptFactoryBuilder(container);

            if (isLoad) {
                this.load();
            }

            this.isInit = true;

        } finally {
            lock.unlock();
        }
    }

    public void load() {

        if (path != null) {
            lock.lock();
            try {
                if (this.path.startsWith("/")) {
                    this.path = this.path.substring(1);
                }

                Reader reader = this.characterEncoding == null ?
                        StreamUtil.readFile(this.path) :
                        StreamUtil.readFile(this.path, this.characterEncoding);

                scriptBuilder.addFactories(reader);
                reader.close();


            } catch (Exception e) {
                log.error("Butterfly::load():{}", e.getMessage());
            } finally {
                lock.unlock();
            }

        }


    }

    @SuppressWarnings("unchecked")
    public <T> T instance(String id, Object... args) {
        return (T) this.getContainer().instance(id, args);
    }


    public void addBCScript(String bcs) {
        StringReader stringReader = new StringReader(bcs);
        this.addFactories(stringReader);
        stringReader.close();
    }

    public void replaceBCScript(String bcs) {
        StringReader stringReader = new StringReader(bcs);
        this.replaceFactories(stringReader);
        stringReader.close();
    }

    public void addFactories(Reader reader) {
        this.getScriptBuilder().addFactories(reader);
    }

    public void replaceFactories(Reader reader) {
        this.getScriptBuilder().replaceFactories(reader);
    }


    public void addFactory(String factoryScript) {
        this.getScriptBuilder().addFactory(factoryScript);
    }

    public void replaceFactory(String factoryScript) {
        this.getScriptBuilder().replaceFactory(factoryScript);
    }


    public void init() {
        this.getContainer().init();
    }


    public void execPhase(String phase) {
        this.getContainer().execPhase(phase);
    }

    public void execPhase(String phase, String name) {
        this.getContainer().execPhase(phase, name);
    }


    public void dispose() {
        this.getContainer().dispose();
    }


    public IContainer getContainer() {
        if (!isInit) {
            lock.lock();
            try {
                if (!isInit) {
                    this.build();
                }
            } finally {
                lock.unlock();
            }
        }
        return container;
    }

    public ScriptFactoryBuilder getScriptBuilder() {
        if (!isInit) {
            lock.lock();
            try {
                if (!isInit) {
                    this.build();
                }
            } finally {
                lock.unlock();
            }
        }
        return scriptBuilder;
    }


    public void setBCSPath(String path) {
        lock.lock();
        try {
            this.path = path;
        } finally {
            lock.unlock();
        }

    }

    public void setLoad(boolean isLoad) {
        this.isLoad = isLoad;
    }


}
