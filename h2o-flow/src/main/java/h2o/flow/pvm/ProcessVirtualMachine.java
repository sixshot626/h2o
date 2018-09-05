/**
 * @author 张建伟
 */
package h2o.flow.pvm;


import h2o.common.collections.CollectionUtil;
import h2o.common.collections.builder.ListBuilder;
import h2o.common.collections.tuple.Tuple2;
import h2o.common.exception.ExceptionUtil;
import h2o.flow.pvm.elements.Line;
import h2o.flow.pvm.elements.Node;
import h2o.flow.pvm.elements.SignalNode;
import h2o.flow.pvm.runtime.RunContext;
import h2o.flow.pvm.runtime.RunStatus;
import h2o.flow.pvm.runtime.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

public final class ProcessVirtualMachine {

    private static final Logger log = LoggerFactory.getLogger( ProcessVirtualMachine.class.getName() );


    //=================================================
	//  运行监听器
	//=================================================
	
	private final List<ProcessRunListener> processRunListeners = ListBuilder.newList();
	
	public ProcessVirtualMachine addProcessRunListener( ProcessRunListener... processRunListeners ) {
		if( !CollectionUtil.argsIsBlank(processRunListeners) ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				this.processRunListeners.add(processRunListener);
			}
		}
		return this;
	}
	
	public ProcessVirtualMachine addProcessRunListeners( Collection<? extends ProcessRunListener> processRunListeners ) {
		if( CollectionUtil.isNotBlank( processRunListeners ) ) {
			this.processRunListeners.addAll( processRunListeners );
		}
		return this;
	}
	
	private void fireStart( RunContext runContext , boolean signal ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.onStart(runContext , signal );
			}
		}
	}
	
	private void fireInNode(  RunContext runContext , Node node ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.inNode( runContext , node );
			}
		}
	}
	
	private void fireOutNode(  RunContext runContext , Node node , RunStatus runStatus , List<Line> lines ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.outNode( runContext , node , runStatus , lines );
			}
		}
	}
	
	private void firePassLine( RunContext runContext , Line line ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.passLine(runContext, line);
			}
		}
	}
	
	private void fireEnd( RunContext runContext , RunStatus runStatus ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.onEnd(runContext , runStatus );
			}
		}
	}
	
	private void fireException( RunContext runContext , Throwable e ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.onException(runContext, e);
			}
		}
	}
	
	
	
	
	//=================================================
	//  核心实现
	//=================================================

	
	public  RunStatus start( RunContext runContext ) throws FlowException {
		return run( runContext , runContext.getFlowInstance().findStartNode() , false );
	}
	
	public  RunStatus run(  RunContext runContext ,  Object nodeId ) throws FlowException {
		return run( runContext , runContext.getFlowInstance().findNode( nodeId ) , true );
	}
	

	private RunStatus run(  RunContext runContext , Node node , boolean isSignal ) throws FlowException  {		
	
		
		TransactionManager tx = runContext.getTxManager();
		
		if( tx != null ) {
			tx.beginTransaction();
		}
		
		try {
			
			fireStart( runContext , isSignal );
			
			Engine engine = new Engine();
			
			engine.runNode( runContext , node , isSignal  );
			
			fireEnd( runContext , engine.getRunStatus() );
			
			if( tx != null ) {
				tx.commit();
			}
			
			return engine.getRunStatus();
			
		} catch( Throwable e ) {	
			
			fireException( runContext , e );
			fireEnd( runContext , RunStatus.EXCEPTION );
			
			if( tx != null ) try {
				tx.rollBack();
			} catch( Exception e1 ) {
				e1.printStackTrace();
			}
			
			
			e.printStackTrace();
			
			log.error("", e);
			
			if( e instanceof FlowException  ) {
				throw (FlowException) e;
			} else {
				throw ExceptionUtil.toRuntimeException( e );
			}
			
			
		}
		
	}
	
	
	//=======================================
	// 流程发动机
	//=======================================
	
	private class Engine {
		
		private RunStatus runStatus = RunStatus.RUNNING;	
		
		
		public RunStatus getRunStatus() {
			return runStatus;
		}



		public void runNode( RunContext runContext ,  Node node , boolean isSignal  ) throws FlowException {
			
			fireInNode( runContext , node );
			
			Tuple2<RunStatus, List<Line>> r = isSignal ? ((SignalNode)node).signal(runContext) : node.exec(runContext);
			
			fireOutNode( runContext , node  , r.e0 , r.e1 );
			
			if( r.e0 == RunStatus.RUNNING && this.runStatus != RunStatus.END ) {
				
				int n = r.e1.size();
				for( Line line : r.e1 ) {					
					
					RunContext nextRunContext = n > 1 ? runContext.copy() : runContext;					
					
					firePassLine( nextRunContext , line );
					
					Node nextNode = line.pass( nextRunContext );
					runNode( nextRunContext , nextNode , false  );					
					
					if( this.runStatus == RunStatus.END ) {
						break;
					}
				}			
				
			} else {
				this.runStatus = r.e0;
			}
			
		}
		
	}
	

}
