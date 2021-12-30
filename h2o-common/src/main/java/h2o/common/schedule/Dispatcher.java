package h2o.common.schedule;

import h2o.common.concurrent.Door;
import h2o.common.concurrent.RunUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Dispatcher {

    private static final Logger log = LoggerFactory.getLogger( Dispatcher.class.getName() );
	
	private final long firstDelay;
	private final long okSleepTime;
	private final long freeSleepTime;
	private final long errSleepTime;

	private final RepetitiveTask task;


	public Dispatcher(RepetitiveTask task , long firstDelay, long okSleepTime, long freeSleepTime, long errSleepTime) {
		this.task = task;
		this.firstDelay = firstDelay;
		this.okSleepTime = okSleepTime;
		this.freeSleepTime = freeSleepTime;
		this.errSleepTime = errSleepTime;
	}

	private volatile boolean stop = false;
	private volatile boolean running = false;

	private final Door door = new Door( true );


	public void stop() {
		stop = true;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void wakeup() {
		door.open();
	}
	
	
	
	public Future<?> start() {

		if ( stop ) {
			throw new IllegalStateException( "Service is stopped!" );
		}
		if ( running ) {
			throw new IllegalStateException( "Service is running!" );
		}
		
		running = true;
		
		return RunUtil.run( new Runnable() {

			public void run() {
				
				try {
				
					sleep(firstDelay);

					while( !stop ) {
						
						long st;
						
						try {
                            TaskResult tr = task.doTask();
							if( tr.taskState == TaskState.Free ) {
								st = freeSleepTime;
							} else if( tr.taskState == TaskState.Ok ) {
								st = okSleepTime;
							} else if( tr.taskState == TaskState.Continue ) {
								st = 0;
							} else if( tr.taskState == TaskState.Wait ) {
								st = -1;
							} else if( tr.taskState == TaskState.Break ) {
								break;
							} else {
								st = tr.getSleepTime();
							}
							
						} catch( InterruptedException e ) {
							throw e;
						} catch( Throwable e) {
							log.error("Dispatcher-run",e);
							st = errSleepTime;
						}

                        sleep(st);

					}
					
				
				} catch( InterruptedException e ) {
					log.info("Interrupted", e);
				}
				
				stop = true;
				running = false;

			}
			
		} );
		
	}
	
	
	private void sleep( long st ) throws InterruptedException {
		
		if( st != 0 ) { 
			
			try {
				door.close();
				if( st > 0L ) {
					door.await(st, TimeUnit.MILLISECONDS);
				} else {
					door.await();
				}
				
			} catch( InterruptedException e) {
				throw e;
			} catch (Throwable e) {
				log.debug("Sleepping", e);
			} finally {
				door.open();
			}
		
		}
	}


}
