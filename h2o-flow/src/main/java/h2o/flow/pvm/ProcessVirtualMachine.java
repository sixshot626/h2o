/**
 * @author 张建伟
 */
package h2o.flow.pvm;


import h2o.common.exception.ExceptionUtil;
import h2o.common.lang.Val;
import h2o.common.lang.Var;
import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.collection.ListBuilder;
import h2o.flow.pvm.elements.Line;
import h2o.flow.pvm.elements.Node;
import h2o.flow.pvm.elements.SignalNode;
import h2o.flow.pvm.exception.FlowException;
import h2o.flow.pvm.exception.NoLineExcepion;
import h2o.flow.pvm.runtime.FlowTransactionManager;
import h2o.flow.pvm.runtime.ProcessRunListener;
import h2o.flow.pvm.runtime.RuntimeScopeObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class ProcessVirtualMachine {

    private static final Logger log = LoggerFactory.getLogger( ProcessVirtualMachine.class.getName() );


    //=================================================
	//  运行监听器
	//=================================================
	
	private final List<ProcessRunListener> processRunListeners;

	private final FlowTransactionManager transactionManager;


	public ProcessVirtualMachine( ProcessRunListener... processRunListeners ) {
		this( null , processRunListeners);
	}

	public ProcessVirtualMachine( Collection<? extends ProcessRunListener> processRunListeners  ) {
		this( null , processRunListeners);
	}
	
	public ProcessVirtualMachine( FlowTransactionManager transactionManager , ProcessRunListener... processRunListeners  ) {
		this.transactionManager = transactionManager;
		this.processRunListeners = CollectionUtil.argsIsBlank( processRunListeners ) ? Collections.EMPTY_LIST :
						Collections.unmodifiableList( Arrays.asList( processRunListeners ) );
	}
	
	public ProcessVirtualMachine( FlowTransactionManager transactionManager , Collection<? extends ProcessRunListener> processRunListeners  ) {
		this.transactionManager = transactionManager;
		this.processRunListeners = CollectionUtil.isBlank( processRunListeners ) ? Collections.EMPTY_LIST :
				Collections.unmodifiableList( ListBuilder.newListAndAddAll( processRunListeners ) );

	}



	
	private void fireStartEvent(RunContext runContext , Node node , boolean signal , Object[] args ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.onStart(runContext , node , signal , args );
			}
		}
	}
	
	private void fireEnterNodeEvent(RuntimeScopeObject nodeRunScoeObject , RunContext runContext , Node node , boolean signal , Object[] args ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.enterNode( nodeRunScoeObject , runContext , node , signal , args );
			}
		}
	}
	
	private void fireLeaveNodeEvent(RuntimeScopeObject nodeRunScoeObject , RunContext runContext , Node node , ExecResult execResult ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.leaveNode( nodeRunScoeObject ,  runContext , node , execResult );
			}
		}
	}
	
	private void fireEnterLineEvent( RuntimeScopeObject lineRunScoeObject , RunContext runContext , Line line , Object[] args ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.enterLine( lineRunScoeObject , runContext , line , args );
			}
		}
	}

	private void fireLeaveLineEvent( RuntimeScopeObject lineRunScoeObject , RunContext runContext , Line line , Node target ) {
		if( ! processRunListeners.isEmpty() ) {
			for( ProcessRunListener processRunListener : processRunListeners ) {
				processRunListener.leaveLine( lineRunScoeObject , runContext , line , target );
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
	
	



	//=================================================
	//  核心实现
	//=================================================

	
	public ExecResult start( RunContext runContext , Object... args ) throws FlowException {
		return exec( runContext , runContext.getFlowInstance().getStartNode() , false , args );
	}
	
	public ExecResult run( RunContext runContext , Object nodeId , Object... args ) throws FlowException {
		return exec( runContext , runContext.getFlowInstance().findNode( nodeId )  , false , args );
	}

	public ExecResult signal( RunContext runContext , Object nodeId , Object... args ) throws FlowException {
		return exec( runContext , runContext.getFlowInstance().findNode( nodeId )  , true , args);
	}
	

	private ExecResult exec( RunContext runContext , Node node , boolean isSignal , Object... args ) throws FlowException  {

		FlowTransactionManager tx = this.transactionManager;

		Object transactionObj = ( tx == null ? null : tx.beginTransaction(runContext) );

		try {
			
			fireStartEvent( runContext , node , isSignal , args );
			
			Engine engine = new Engine();

			engine.runNode( runContext, node, isSignal , args );

			ExecResult result = engine.getExecResult();

			fireEndEvent( runContext , result );
			
			if( tx != null ) {
				tx.commit( transactionObj );
			}

			return result;
			
		} catch( Throwable e ) {	

			try {

				fireExceptionEvent(runContext, e);
				fireEndEvent(runContext, new ExecResult(RunStatus.EXCEPTION));

				log.error("", e);

			} catch ( Exception el ) {
				log.error("", el);
			} finally {
				if( tx != null ) try {
					tx.rollBack( transactionObj );
				} catch( Exception er ) {
					log.error("", er);
				}
			}

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

		private Val<Object> result = Val.empty();
		private RunStatus runStatus = RunStatus.RUNNING;

		public ExecResult getExecResult() {
			return new ExecResult( result , runStatus );
		}

		public ExecResult runNode( RunContext runContext , Node node , boolean isSignal , Object... args ) throws FlowException {

			RuntimeScopeObject nodeRunScoeObject = new RuntimeScopeObject();

			fireEnterNodeEvent( nodeRunScoeObject , runContext, node , isSignal , args );

			final ExecResult nodeExecResult = isSignal ? ( (SignalNode) node).signal( runContext , args ) : node.exec( runContext , args );
			if ( !this.result.isSetted() && nodeExecResult.getResult().isSetted() ) {
				this.result = nodeExecResult.getResult();
			}

			fireLeaveNodeEvent(nodeRunScoeObject , runContext, node, nodeExecResult );

			if( nodeExecResult.getStatus() == RunStatus.RUNNING && this.runStatus != RunStatus.END ) {

				List<Line> lines = nodeExecResult.getLines();
				if( CollectionUtil.isBlank( lines ) ) {
					throw new NoLineExcepion();
				}

				int n = nodeExecResult.getLines().size();
				for( Line line : nodeExecResult.getLines() ) {

					RunContext nextRunContext = n > 1 ? runContext.copy() : runContext;



					RuntimeScopeObject lineRunScoeObject = new RuntimeScopeObject();

					fireEnterLineEvent(lineRunScoeObject , nextRunContext , line , args );

					Node nextNode = line.pass( nextRunContext , args );

					fireLeaveLineEvent(lineRunScoeObject , nextRunContext , line , nextNode );


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
