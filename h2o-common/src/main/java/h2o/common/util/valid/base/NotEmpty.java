package h2o.common.util.valid.base;

import h2o.common.util.valid.Validator;
import org.apache.commons.lang.StringUtils;

public class NotEmpty extends AbstractValidator implements Validator {
	
	private boolean nullAble = false;

	public NotEmpty() {}
	
	public NotEmpty( boolean nullAble ) {
		this.nullAble = nullAble;
	}
	
	@Override
	public boolean validateV( Object v ) {
		
		if( v == null ) {
			return nullAble;
		}		
		
		return StringUtils.isNotEmpty((String)v);
		
	}

}
