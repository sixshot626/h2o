package h2o.common;

import h2o.common.util.bean.BeanUtil;
import h2o.common.util.bean.support.JoddBeanUtilVOImpl;


public class Tools {

	public static final BeanUtil b = new BeanUtil(new JoddBeanUtilVOImpl(true,false) , null);
	
	public static final Logger log = new Logger();

}
