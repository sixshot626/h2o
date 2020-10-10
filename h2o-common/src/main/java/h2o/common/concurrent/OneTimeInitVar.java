package h2o.common.concurrent;

public class OneTimeInitVar<T> implements java.io.Serializable {	

	private static final long serialVersionUID = -1482561103424372788L;

	private volatile T var;
	
	private final OneTimeInitVar<?> flag;
	
	private final String errMsg;
	
	public OneTimeInitVar() {
		this.flag = null;
		this.errMsg = "Multiple assignments";
	}
	
	public OneTimeInitVar( String errMsg ) {
		this.flag 	= null;
		this.errMsg = errMsg;
	}
	
	public OneTimeInitVar( String errMsg , T defVal ) {
		this.flag 	= null;
		this.errMsg = errMsg;
		this.var 	= defVal;
	}
	
	public OneTimeInitVar( OneTimeInitVar<?> flag ) {
		this.flag 	= flag;
		this.errMsg = flag.errMsg;
	}
	
	public OneTimeInitVar( OneTimeInitVar<?> flag , T defVal ) {
		this.flag 	= flag;
		this.errMsg = flag.errMsg;
		this.var 	= defVal;
	}

	public T getVar() {
		return var;
	}
	
	public boolean isSetted() {
		if( flag == null ) {
			T _var = this.var;
			return _var != null;
		} else {
			return flag.isSetted();
		}
	}
	
	public boolean setVar(T var ) {
		return this.setVar(var, false);
	}

	public boolean setVar(T var , boolean isSilently ) {
		if( isSetted() ) {
			if( isSilently || errMsg == null ) {				
				return false;
			} else {
				throw new RuntimeException(errMsg);				
			}
		}
		synchronized (this) {
			if( isSetted() ) {
				if( isSilently || errMsg == null ) {
					return false;
				} else {
					throw new RuntimeException(errMsg);
				}
			}
			this.var = var;
		}
		return true;
	}
	

	public String getErrMsg() {
		return errMsg;
	}

	@SuppressWarnings("unchecked")
	public <F> OneTimeInitVar<F> getFlag() {
		return (OneTimeInitVar<F>)flag;
	}

	@Override
	public String toString() {
		return var == null ? "<null>" : var.toString();
	}
	


}
