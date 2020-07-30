/**
 * @author 张建伟
 */
package h2o.flow.pvm;


import h2o.common.collections.CollectionUtil;
import h2o.common.collections.builder.ListBuilder;
import h2o.common.exception.ExceptionUtil;
import h2o.flow.pvm.elements.Line;
import h2o.flow.pvm.elements.Node;
import h2o.flow.pvm.runtime.NodeExecResult;
import h2o.flow.pvm.elements.SignalNode;
import h2o.flow.pvm.runtime.FlowTransactionManager;
import h2o.flow.pvm.runtime.NodeRunScoeObject;
import h2o.flow.pvm.runtime.RunContext;
import h2o.flow.pvm.runtime.RunStatus;
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
	
	private void fireStartEvent(RunContext runContext , boolean signal ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.onStart(runContext , signal );
			}
		}
	}
	
	private void fireEnterNodeEvent( NodeRunScoeObject nodeRunScoeObject , RunContext runContext , Node node ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.enterNode( nodeRunScoeObject , runContext , node );
			}
		}
	}
	
	private void fireLeaveNodeEvent( NodeRunScoeObject nodeRunScoeObject , RunContext runContext , Node node , RunStatus runStatus , List<Line> lines ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.leaveNode( nodeRunScoeObject ,  runContext , node , runStatus , lines );
			}
		}
	}
	
	private void firePassLineEvent(RunContext runContext , Line line ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.passLine(runContext, line);
			}
		}
	}
	
	private void fireEndEvent(RunContext runContext , RunStatus runStatus ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.onEnd(runContext , runStatus );
			}
		}
	}
	
	private void fireExceptionEvent(RunContext runContext , Throwable e ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.onException(runContext, e);
			}
		}
	}
	
	


	private volatile FlowTransactionManager transactionManager;

	public void setTransactionManager(FlowTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	//=================================================
	//  核心实现
	//=================================================

	
	public  RunStatus start( RunContext runContext  ) throws FlowException {
		return run( runContext , runContext.getFlowInstance().getStartNode() , false );
	}
	
	public  RunStatus run(  RunContext runContext ,  Object nodeId  ) throws FlowException {
		return run( runContext , runContext.getFlowInstance().findNode( nodeId )  , true );
	}
	

	private RunStatus run(  RunContext runContext , Node node  , boolean isSignal ) throws FlowException  {

		FlowTransactionManager tx = this.transactionManager;

		Object transactionObj = tx == null ? null : tx.beginTransaction(runContext);

		runContext.setTransactionStatus( transactionObj );

		
		try {
			
			fireStartEvent( runContext , isSignal );
			
			Engine engine = new Engine();
			
			engine.runNode( runContext , node , isSignal  );
			
			fireEndEvent( runContext , engine.getRunStatus() );
			
			if( tx != null ) {
				tx.commit( transactionObj );
			}
			
			return engine.getRunStatus();
			
		} catch( Throwable e ) {	
			
			fireExceptionEvent( runContext , e );
			fireEndEvent( runContext , RunStatus.EXCEPTION );
			
			if( tx != null ) try {
				tx.rollBack( transactionObj );
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



		public void runNode( RunContext runContext ,  Node node , boolean isSignal ) throws FlowException {

			NodeExecResult nodeExecResult = null;
			{
				NodeRunScoeObject nodeRunScoeObject = new NodeRunScoeObject();

				fireEnterNodeEvent(nodeRunScoeObject , runContext, node);

				nodeExecResult = isSignal ? ((SignalNode) node).signal(runContext) : node.exec(runContext);

				fireLeaveNodeEvent(nodeRunScoeObject , runContext, node, nodeExecResult.getStatus(), nodeExecResult.getLines());

			}
			
			if( nodeExecResult.getStatus() == RunStatus.RUNNING && this.runStatus != RunStatus.END ) {
				
				int n = nodeExecResult.getLines().size();
				for( Line line : nodeExecResult.getLines() ) {
					
					RunContext nextRunContext = n > 1 ? runContext.copy() : runContext;					
					
					firePassLineEvent( nextRunContext , line );
					
					Node nextNode = line.pass( nextRunContext );
					runNode( nextRunContext , nextNode , false  );					
					
					if( this.runStatus == RunStatus.END ) {
						break;
					}
				}			
				
			} else {
				this.runStatus = nodeExecResult.getStatus();
			}
			
		}
		
	}
	

}
