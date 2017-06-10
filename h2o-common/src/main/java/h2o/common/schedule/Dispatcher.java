package h2o.common.schedule;

import h2o.common.Tools;
import h2o.common.concurrent.Door;
import h2o.common.concurrent.Locks;
import h2o.common.concurrent.OneTimeInitVar;
import h2o.common.concurrent.RunUtil;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class Dispatcher {
	

	private final Door door = new Door( true );	
	
	private final OneTimeInitVar<Boolean> f = new OneTimeInitVar<Boolean>("Has started");
	
	private final OneTimeInitVar<Long> firstDelay 	 = new OneTimeInitVar<Long>( f ,  0L );
	
	private final OneTimeInitVar<Long> okSleepTime	 = new OneTimeInitVar<Long>( f ,  100L );
	private final OneTimeInitVar<Long> freeSleepTime = new OneTimeInitVar<Long>( f ,  60000L );
	private final OneTimeInitVar<Long> errSleepTime  = new OneTimeInitVar<Long>( f ,  60000L );
	
	private final OneTimeInitVar<RepetitiveTask>  task = new OneTimeInitVar<RepetitiveTask>( f ,  null);	
	

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
		
		f.setVar(true);
		running = true;
		
		return RunUtil.call( new Runnable() {

			public void run() {
				
				try {
				
					sleep(firstDelay.getVar());
					

					while( !stop ) {
						
						long st;
						
						try {
							int r =  task.getVar().doTask();
							if( r == 0 ) {
								st = freeSleepTime.getVar();
							} else if(r == 1 ) {
								st = okSleepTime.getVar();
							} else if(r == 2 ) {
								st = 0;
							} else if(r < 0 ) {
								st = -1;
							} else if(r == 9 ) {
								break;
							} else {
								st = r;
							}
							
						} catch( InterruptedException e ) {

						    if( interruptible ) {
                                throw e;
                            } else {
						        Thread.currentThread().interrupt();
                                st = errSleepTime.getVar();
                            }

						} catch( Throwable e) {

							Tools.log.debug("Dispatcher-run",e);
							st = errSleepTime.getVar();

						}

                        sleep(st);

					}
					
				
				} catch( InterruptedException e ) {
					Tools.log.error("InterruptedException", e);
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
                } else {
                    Thread.currentThread().interrupt();
                }
			} catch (Throwable e) {
				Tools.log.debug("Sleepping", e);
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

			this.task.setVar(task);		
			
			if( freeSleepTime > 0 ) this.freeSleepTime.setVar(freeSleepTime) ;		
			if( okSleepTime   > 0 ) this.okSleepTime.setVar(okSleepTime);
			if( errSleepTime  > 0 ) this.errSleepTime.setVar(errSleepTime);		
			
			return this.start();
		
		} finally {
			lock.unlock();
		}
		
		
	}
	
	
	
	
	
	public void setTask(RepetitiveTask task) {
		this.task.setVar(task);
	}

	public void setFirstDelay(long firstDelay) {
		this.firstDelay.setVar(firstDelay);
	}
	
	public void setFreeSleepTime(long freeSleepTime) {
		this.freeSleepTime.setVar(freeSleepTime) ;	
	}
	
	public void setOkSleepTime(long okSleepTime) {
		this.okSleepTime.setVar(okSleepTime);
	}
	
	public void setErrSleepTime(long errSleepTime) {
		this.errSleepTime.setVar(errSleepTime);
	}

    public void setInterruptible( boolean interruptible ) {
        this.interruptible = interruptible;
    }
}
