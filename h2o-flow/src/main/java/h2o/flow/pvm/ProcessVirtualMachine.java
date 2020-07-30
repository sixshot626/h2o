/**
 * @author 张建伟
 */
package h2o.flow.pvm;


import h2o.common.collections.CollectionUtil;
import h2o.common.collections.builder.ListBuilder;
import h2o.common.exception.ExceptionUtil;
import h2o.common.thirdparty.spring.util.Assert;
import h2o.flow.pvm.elements.Line;
import h2o.flow.pvm.elements.Node;
import h2o.flow.pvm.runtime.*;
import h2o.flow.pvm.elements.SignalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
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
	
	private void fireStartEvent(RunContext runContext , Node node , boolean signal , Object[] args ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.onStart(runContext , node , signal , args );
			}
		}
	}
	
	private void fireEnterNodeEvent( NodeRunScoeObject nodeRunScoeObject , RunContext runContext , Node node , boolean signal , Object[] args ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.enterNode( nodeRunScoeObject , runContext , node , signal , args );
			}
		}
	}
	
	private void fireLeaveNodeEvent( NodeRunScoeObject nodeRunScoeObject , RunContext runContext , Node node , ExecResult execResult ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.leaveNode( nodeRunScoeObject ,  runContext , node , execResult );
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
	
	private void fireEndEvent(RunContext runContext , ExecResult execResult ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.onEnd(runContext , execResult );
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

	
	public Object start( RunContext runContext , Object... args ) throws FlowException {
		return exec( runContext , runContext.getFlowInstance().getStartNode() , false , args );
	}
	
	public Object run( RunContext runContext , Object nodeId , Object... args ) throws FlowException {
		return exec( runContext , runContext.getFlowInstance().findNode( nodeId )  , false , args );
	}

	public Object signal( RunContext runContext , Object nodeId , Object... args ) throws FlowException {
		return exec( runContext , runContext.getFlowInstance().findNode( nodeId )  , true , args);
	}
	

	private Object exec( RunContext runContext , Node node , boolean isSignal , Object... args ) throws FlowException  {

		FlowTransactionManager tx = this.transactionManager;

		Object transactionObj = tx == null ? null : tx.beginTransaction(runContext);

		runContext.setTransactionStatus( transactionObj );

		
		try {
			
			fireStartEvent( runContext , node , isSignal , args );
			
			Engine engine = new Engine();

			ExecResult execResult = engine.runNode( runContext, node, isSignal , args );
			
			fireEndEvent( runContext , execResult );
			
			if( tx != null ) {
				tx.commit( transactionObj );
			}

			return execResult.getResult();
			
		} catch( Throwable e ) {	
			
			fireExceptionEvent( runContext , e );
			fireEndEvent( runContext , new ExecResult( RunStatus.EXCEPTION , Collections.EMPTY_LIST ) );
			
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

		public ExecResult runNode( RunContext runContext , Node node , boolean isSignal , Object... args ) throws FlowException {

			NodeRunScoeObject nodeRunScoeObject = new NodeRunScoeObject();

			fireEnterNodeEvent( nodeRunScoeObject , runContext, node , isSignal , args );

			final ExecResult nodeExecResult = isSignal ? ( (SignalNode) node).signal( runContext , args ) : node.exec( runContext , args );

			fireLeaveNodeEvent(nodeRunScoeObject , runContext, node, nodeExecResult );

			if( nodeExecResult.getStatus() == RunStatus.RUNNING && this.runStatus != RunStatus.END ) {

				List<Line> lines = nodeExecResult.getLines();
				if( CollectionUtil.isBlank( lines ) ) {
					throw new NoLineExcepion();
				}

				int n = nodeExecResult.getLines().size();
				for( Line line : nodeExecResult.getLines() ) {

					RunContext nextRunContext = n > 1 ? runContext.copy() : runContext;

					firePassLineEvent( nextRunContext , line );

					Node nextNode = line.pass( nextRunContext );
					runNode( nextRunContext , nextNode , false , args );

					if( this.runStatus == RunStatus.END ) {
						break;
					}
				}

			} else {
				this.runStatus = nodeExecResult.getStatus();
			}

			return nodeExecResult;

		}

	}
	

}
