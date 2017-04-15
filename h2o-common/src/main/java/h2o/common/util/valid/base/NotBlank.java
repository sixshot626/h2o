package h2o.common.util.valid.base;

import h2o.common.util.valid.Validator;
import org.apache.commons.lang.StringUtils;

public class NotBlank extends AbstractValidator implements Validator {
	

	private boolean nullAble = false;

	public NotBlank() {}
	
	public NotBlank( boolean nullAble ) {
		this.nullAble = nullAble;
	}
	
	@Override
	public boolean validateV( Object v ) {
		
		if( v == null ) {
			return nullAble;
		}
		
		return StringUtils.isNotBlank((String)v);
		
	}

}
