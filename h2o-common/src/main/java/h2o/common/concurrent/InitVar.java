package h2o.common.concurrent;

public class InitVar<T> implements java.io.Serializable {

	private static final long serialVersionUID = -1482561103424372788L;

	private volatile T value;
	
	private final InitVar<?> flag;
	
	private final String errMsg;
	
	public InitVar() {
		this.flag = null;
		this.errMsg = null;
	}
	
	public InitVar(String errMsg ) {
		this.flag 	= null;
		this.errMsg = errMsg;
	}
	
	public InitVar(String errMsg , T defVal ) {
		this.flag 	= null;
		this.errMsg = errMsg;
		this.value = defVal;
	}
	
	public InitVar(InitVar<?> flag ) {
		this.flag 	= flag;
		this.errMsg = flag.errMsg;
	}
	
	public InitVar(InitVar<?> flag , T defVal ) {
		this.flag 	= flag;
		this.errMsg = flag.errMsg;
		this.value = defVal;
	}

	public T getValue() {
	    T _v = this.value;
		return _v;
	}
	
	public boolean isSetted() {
		if( flag == null ) {
			T _var = this.value;
			return _var != null;
		} else {
			return flag.isSetted();
		}
	}
	
	public boolean setValue( T value ) {
		return this.setValue(value, false);
	}

	public boolean setValue(T value , boolean isSilently ) {
		if( isSetted() ) {
			if( isSilently || errMsg == null ) {				
				return false;
			} else {
				throw new IllegalStateException(errMsg);
			}
		}
		synchronized (this) {
			if( isSetted() ) {
				if( isSilently || errMsg == null ) {
					return false;
				} else {
					throw new IllegalStateException(errMsg);
				}
			}
			this.value = value;
		}
		return true;
	}
	

	public String getErrMsg() {
		return errMsg;
	}

	@SuppressWarnings("unchecked")
	public <F> InitVar<F> getFlag() {
		return (InitVar<F>)flag;
	}

	@Override
	public String toString() {
		return value == null ? "<null>" : value.toString();
	}
	


}
