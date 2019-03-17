package h2o.common;

import h2o.common.util.bean.BeanUtil;
import h2o.common.util.bean.support.CasePreOperateImpl;
import h2o.common.util.bean.support.JoddBeanUtilVOImpl;
import h2o.common.util.bean.support.MapVOImpl;


public class Tools {

	public static final BeanUtil b = new BeanUtil(new JoddBeanUtilVOImpl(true,false) , null);
	
	public static final Logger log = new Logger();

}
