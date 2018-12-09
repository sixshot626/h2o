package h2o.event.impl;


import h2o.common.schedule.Dispatcher;
import h2o.common.schedule.RepetitiveTask;
import h2o.common.schedule.TaskResult;
import h2o.event.Event;
import h2o.event.EventReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by zjw on 16-6-30.
 */
public abstract class AbstractDispatcherEventReceiver extends AbstractEventReceiver implements EventReceiver {

    private static final Logger log = LoggerFactory.getLogger( AbstractDispatcherEventReceiver.class.getName() );


    private final Dispatcher d = new Dispatcher();

    public AbstractDispatcherEventReceiver( long freeSleepTime, long okSleepTime, long errSleepTime ) {
        d.setFreeSleepTime(freeSleepTime);
        d.setOkSleepTime(okSleepTime);
        d.setErrSleepTime(errSleepTime);
    }

    @Override
    public void start() {

        log.info("EventReceiver start...");

        d.start( new RepetitiveTask() {
            @Override
            public TaskResult doTask() throws Throwable {

                List<Event> events = recvEvent();
                if( events == null || events.isEmpty() ) {
                    return TaskResult.FREE;
                } else {
                    for( Event event : events ) {
                        procEvent(event);
                    }
                    return TaskResult.OK;
                }


            }
        });
    }


    protected void procEvent( Event event) {
        eventProcessor.proc( new NothingEventContext() , event);
    }


    @Override
    public void stop() {
        d.stop();
    }


    protected abstract List<Event> recvEvent();


}
