package h2o.common;

import h2o.common.util.bean.BeanUtil;
import h2o.common.util.bean.support.JoddBeanUtilVOImpl;


public class Tools {

	public static final BeanUtil b = new BeanUtil(new JoddBeanUtilVOImpl(true,false) , null);

    public static final BeanUtil bic = b.ignoreCase(true);

	
	public static final Logger log = new Logger();

}
