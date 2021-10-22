package h2o.common.schedule;

import h2o.common.concurrent.Door;
import h2o.common.concurrent.InitVar;
import h2o.common.concurrent.Locks;
import h2o.common.concurrent.RunUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class Dispatcher {

    private static final Logger log = LoggerFactory.getLogger( Dispatcher.class.getName() );


    private final Door door = new Door( true );
	
	private final InitVar<Boolean> f = new InitVar<Boolean>("Has been started!");
	
	private final InitVar<Long> firstDelay 	 = new InitVar<Long>( f ,  0L );
	
	private final InitVar<Long> okSleepTime	 = new InitVar<Long>( f ,  100L );
	private final InitVar<Long> freeSleepTime = new InitVar<Long>( f ,  60000L );
	private final InitVar<Long> errSleepTime  = new InitVar<Long>( f ,  60000L );
	
	private final InitVar<RepetitiveTask> task = new InitVar<RepetitiveTask>( f ,  null);
	

	private volatile boolean stop = false;
	private volatile boolean running = false;
	private volatile boolean interruptible = false;


	private final Lock lock = Locks.newLock();
	

	public void stop() {
		stop = true;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void wakeup() {
		door.open();
	}
	
	

	
	private Future<?> startTask() {		
		
		f.setValue(true);
		running = true;
		
		return RunUtil.run( new Runnable() {

			public void run() {
				
				try {
				
					sleep(firstDelay.getValue());
					

					while( !stop ) {
						
						long st;
						
						try {
                            TaskResult tr = task.getValue().doTask();
							if( tr.taskState == TaskState.Free ) {
								st = freeSleepTime.getValue();
							} else if( tr.taskState == TaskState.Ok ) {
								st = okSleepTime.getValue();
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

						    if( interruptible ) {
                                throw e;
                            } else {
                                st = errSleepTime.getValue();
                            }

						} catch( Throwable e) {

							log.error("Dispatcher-run",e);
							st = errSleepTime.getValue();

						}

                        sleep(st);

					}
					
				
				} catch( InterruptedException e ) {
					log.error("InterruptedException", e);
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
			    if( interruptible ) {
                    throw e;
                }
			} catch (Throwable e) {
				log.debug("Sleepping", e);
			} finally {
				door.open();
			}
		
		}
	}
	
	
	public Future<?> start() {
		
		lock.lock();
		try {		
			return this.startTask();
		} finally {
			lock.unlock();
		}
	}
	
	public Future<?> start( RepetitiveTask task ) {
		lock.lock();
		try {	
			return this.start(task , -1 , -1 , -1);
		} finally {
			lock.unlock();
		}
	}
	
	
	public Future<?> start( RepetitiveTask task , long freeSleepTime , long okSleepTime , long errSleepTime ) {
		
		lock.lock();
		try {

			this.task.setValue(task);
			
			if( freeSleepTime > 0 ) this.freeSleepTime.setValue(freeSleepTime) ;
			if( okSleepTime   > 0 ) this.okSleepTime.setValue(okSleepTime);
			if( errSleepTime  > 0 ) this.errSleepTime.setValue(errSleepTime);
			
			return this.start();
		
		} finally {
			lock.unlock();
		}
		
		
	}




	
	public void setTask(RepetitiveTask task) {
		this.task.setValue(task);
	}

	public void setFirstDelay(long firstDelay) {
		this.firstDelay.setValue(firstDelay);
	}
	
	public void setFreeSleepTime(long freeSleepTime) {
		this.freeSleepTime.setValue(freeSleepTime) ;
	}
	
	public void setOkSleepTime(long okSleepTime) {
		this.okSleepTime.setValue(okSleepTime);
	}
	
	public void setErrSleepTime(long errSleepTime) {
		this.errSleepTime.setValue(errSleepTime);
	}

    public void setInterruptible( boolean interruptible ) {
        this.interruptible = interruptible;
    }


}
