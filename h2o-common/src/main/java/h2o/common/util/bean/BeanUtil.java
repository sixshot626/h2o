package h2o.common.util.bean;

import h2o.common.collections.CollectionUtil;
import h2o.common.collections.IgnoreCaseMap;
import h2o.common.exception.ExceptionUtil;
import h2o.common.thirdparty.spring.util.Assert;
import h2o.common.util.bean.serialize.BeanEncoder;
import h2o.common.util.bean.serialize.BeanSerializer;
import h2o.common.util.bean.support.*;
import h2o.common.util.lang.InstanceUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class BeanUtil {

    private static final Logger log = LoggerFactory.getLogger( BeanUtil.class.getName() );


    private final BeanSerializer beanSerializer =  new BeanEncoder();
	

	private final ValOperate beanVo ;

	private final ValOperate mapVo;
	
	private final boolean cover;
	
	private final boolean procNull;

    private final boolean ignoreCase;
	
	private final BeanDescriptor beanDescriptor;
	private final BeanPropertyInfo beanPropertyInfo;


	public ValOperate getBeanVo() {
		return beanVo;
	}

	public ValOperate getMapVo() {
		return mapVo;
	}

	public boolean isCover() {
		return cover;
	}

	public boolean isProcNull() {
		return procNull;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public BeanDescriptor getBeanDescriptor() {
		return beanDescriptor;
	}

	public BeanPropertyInfo getBeanPropertyInfo() {
		return beanPropertyInfo;
	}



	public BeanUtil() {
		this(null,null );
	}
	
	public BeanUtil( ValOperate beanVo , ValOperate mapVo ) {
	    this( new Builder().setBeanVo(beanVo).setMapVo(mapVo) );
	}



	private BeanUtil( Builder builder ) {

		this.beanVo 	= builder.beanVo;
		this.mapVo  	= builder.mapVo;

		this.cover 		= builder.cover;
		this.procNull 	= builder.procNull;
		this.ignoreCase = builder.ignoreCase;

		this.beanDescriptor 	= builder.beanDescriptor;
		this.beanPropertyInfo 	= builder.beanPropertyInfo;

	}

	public static Builder build() {
		return new Builder();
	}


	public static class Builder {

		private Builder() {}

		private Builder( BeanUtil beanUtil ) {

            this.beanVo 	= beanUtil.beanVo;
            this.mapVo  	= beanUtil.mapVo;

            this.cover 		= beanUtil.cover;
            this.procNull 	= beanUtil.procNull;
            this.ignoreCase = beanUtil.ignoreCase;

            this.beanDescriptor 	= beanUtil.beanDescriptor;
            this.beanPropertyInfo 	= beanUtil.beanPropertyInfo;
        }

		private  ValOperate beanVo = new JoddBeanUtilVOImpl(true,false);

		private  ValOperate mapVo = new MapVOImpl();

		private  boolean cover = true;

		private  boolean procNull = true;

		private  boolean ignoreCase;

		private  BeanDescriptor beanDescriptor = new JoddBeanDescriptorImpl();
		private  BeanPropertyInfo beanPropertyInfo = new DefaultBeanPropertyInfoImpl();

        public Builder setBeanVo(ValOperate beanVo) {
            if ( beanVo != null ) {
                this.beanVo = beanVo;
            }
            return this;
        }

        public Builder setMapVo(ValOperate mapVo) {
            if ( mapVo != null ) {
                this.mapVo = mapVo;
            }
            return this;
        }

        public Builder setCover(boolean cover) {
			this.cover = cover;
			return this;
		}

		public Builder setProcNull(boolean procNull) {
			this.procNull = procNull;
			return this;
		}

		public Builder setIgnoreCase(boolean ignoreCase) {
			this.ignoreCase = ignoreCase;
			return this;
		}

		public Builder setBeanDescriptor(BeanDescriptor beanDescriptor) {
			this.beanDescriptor = beanDescriptor;
			return this;
		}

		public Builder setBeanPropertyInfo(BeanPropertyInfo beanPropertyInfo) {
			this.beanPropertyInfo = beanPropertyInfo;
			return this;
		}

		public BeanUtil get() {
			return new BeanUtil( this );
		}

	}




	public BeanUtil cover( boolean cover ) {
		return new BeanUtil( new Builder(this).setCover( cover ) );
	}

	public BeanUtil procNull( boolean procNull ) {
	    return new BeanUtil( new Builder(this).setProcNull( procNull ) );
	}

    public BeanUtil ignoreCase( boolean ignoreCase ) {
        return new BeanUtil( new Builder(this).setIgnoreCase( ignoreCase ) );
    }



	public BeanUtil beanVo( ValOperate beanVo ) {
        return new BeanUtil(new Builder(this).setBeanVo( beanVo ) );
	}

	public BeanUtil mapVo( ValOperate mapVo ) {
        return new BeanUtil(new Builder(this).setMapVo( mapVo ) );
	}



	
	protected <T> T cerateBean( Class<T> beanClass) {		
		return InstanceUtil.newInstance(beanClass);			
	}
	

	@SuppressWarnings("unchecked")
	public static <T> T cloneBean(T bean) {
		try {
			return (T) BeanUtils.cloneBean(bean);
		} catch (Exception e) {
			log.debug("cloneBean", e);
			throw ExceptionUtil.toRuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends java.io.Serializable> T deepClone(T obj) {
		try {
			byte[] bs = beanSerializer.bean2bytes(obj);
			return (T) beanSerializer.bytes2bean(bs);
		} catch (Exception e) {
			log.debug("deepClone", e);
			throw ExceptionUtil.toRuntimeException(e);
		}
	}

	public <T> T deepCloneBean(T bean) {
		return this.deepCloneBean(bean, (T) null, (String[]) null);
	}

	@SuppressWarnings("unchecked")
	public <T> T deepCloneBean(T srcBean, T bean, String... nkeys) {

		if (bean == null) {
			bean = (T) cerateBean(srcBean.getClass());			
		}

		HashMap<String, Object> m = (HashMap<String, Object>) this.javaBean2Map1(srcBean, nkeys);

		m = this.deepClone(m);

		this.map2JavaBean(m, bean);

		if ( ! CollectionUtil.argsIsBlank(nkeys) ) {
			this.beanCopy(srcBean, bean, nkeys);
		}

		return bean;

	}


	public String[] getPrepNames(Object bean) {
		return this.beanDescriptor.getPrepNames(bean);
	}


	private String[][] procPrepNames(String[] srcPrepnames0, String[] nkeys, String[] skeys, String[] dkeys) {
		
		Collection<String> srcKeys = new HashSet<String>(Arrays.asList(srcPrepnames0));

		Collection<String> bkl = null;
		if ( ! CollectionUtil.argsIsBlank(nkeys) ) {
			bkl = new HashSet<String>(Arrays.asList(nkeys));
		}

		Map<String, String> mk = null;
		if ( ! CollectionUtil.argsIsBlank(skeys) ) {
			mk = new HashMap<String, String>();
			for (int i = 0; i < skeys.length; i++) {
				srcKeys.add(skeys[i]);
				mk.put(skeys[i], dkeys[i]);
			}
		}

		List<String> spl = new ArrayList<String>();
		List<String> dpl = new ArrayList<String>();

		for (String p : srcKeys) {
			if (bkl != null && bkl.contains(p)) {
				continue;
			}

			spl.add(p);
			if (mk != null && mk.containsKey(p)) {
				dpl.add(mk.get(p));
			} else {
				dpl.add(p);
			}

		}

		String[] srcPrepnames = spl.toArray(new String[spl.size()]);
		String[] prepnames = dpl.toArray(new String[dpl.size()]);

		return new String[][] { srcPrepnames, prepnames };
	}
	
	
	public <T> T beanCopy(Object srcBean, T bean, String[] srcPrepnames, String[] prepnames, ValOperate svo, ValOperate vo) {

	    if( ignoreCase ) {

	        if ( srcBean instanceof Map ) {
                Map m = (Map) srcBean;
                if( ! (m instanceof IgnoreCaseMap) ) {
                    srcBean = new IgnoreCaseMap<Object>((Map<String,Object>)m);
                }
            } else {

                srcBean = new IgnoreCaseMap<Object>( this.ignoreCase(false).beanVo(svo).javaBean2Map( srcBean ) );
                svo = this.mapVo;
            }

        }

		return beanCopy( srcBean, bean, srcPrepnames, prepnames, svo, vo , this.cover , this.procNull ,  this.beanPropertyInfo);

	}	


	public static <T> T beanCopy(Object srcBean, T bean, String[] srcPrepnames, String[] prepnames, ValOperate svo, ValOperate vo , boolean cover , boolean procNull , BeanPropertyInfo beanPropertyInfo ) {

        Assert.notNull(srcBean   , "srcBean == null");
        Assert.notNull(bean      , "bean == null");

		if ( CollectionUtil.argsIsBlank(prepnames) ) {
			throw new RuntimeException("prepnames == null");
		}

		if ( CollectionUtil.argsIsBlank(srcPrepnames) || srcPrepnames.length < prepnames.length) {
			throw new RuntimeException("srcPrepnames == null || srcPrepnames.length < prepnames.length");
		}

		for (int i = 0; i < prepnames.length; i++) {

			String spn = srcPrepnames[i];
			String pn = prepnames[i];
			Object v = svo.get(srcBean, spn);
			if ( procNull || v != null ) {
				if( !cover && !beanPropertyInfo.coverAble(bean,pn,vo.get(bean, pn)) ) {
					continue;
				}
				vo.set(bean, pn, v);
			}

		}
		
		return bean;

	}

	public <T> T beanCopy(Object srcBean, T bean, String[] srcPrepnames0, String[] nkeys, String[] skeys, String[] dkeys, ValOperate svo, ValOperate vo) {

        Assert.notNull(srcBean   , "srcBean == null");
        Assert.notNull(bean      , "bean == null");

		if ( CollectionUtil.argsIsBlank(srcPrepnames0) ) {
			srcPrepnames0 = this.getPrepNames(srcBean);
		}

		String[][] pnss = this.procPrepNames(srcPrepnames0, nkeys, skeys, dkeys);

		return this.beanCopy(srcBean, bean, pnss[0], pnss[1], svo, vo);

	}

	public <T> T beanCopy(Object srcBean, T bean) {

        Assert.notNull(srcBean   , "srcBean == null");
        Assert.notNull(bean      , "bean == null");

		return this.beanCopy(srcBean, bean, this.getPrepNames(bean));
	}

	public <T> T beanCopy(Object srcBean, T bean, String... prepnames) {
		return this.beanCopy(srcBean, bean, prepnames, prepnames);
	}

	public <T> T beanCopy(Object srcBean, T bean, String[] srcPrepnames, String[] prepnames) {

        Assert.notNull(srcBean   , "srcBean == null");
        Assert.notNull(bean      , "bean == null");

		if ( CollectionUtil.argsIsBlank(prepnames) ) {
			prepnames = this.getPrepNames(bean);
		}
		if ( CollectionUtil.argsIsBlank(srcPrepnames) ) {
			srcPrepnames = prepnames;
		}

		return this.beanCopy(srcBean, bean, srcPrepnames, prepnames, beanVo, beanVo);
	}

	public <T> T beanCopy3(Object srcBean, T bean, String[] nkeys, String[] skeys, String[] dkeys) {
		return this.beanCopy(srcBean, bean, null, nkeys, skeys, dkeys, beanVo, beanVo);
	}

	public <T> T map2JavaBean(Map<?,?> m, T bean) {

        Assert.notNull(m        , "map  == null");
        Assert.notNull(bean     , "bean == null");

		return this.map2JavaBean(m, bean, this.getPrepNames(bean));
	}


	public <T> T map2JavaBean(Map<?,?> m, T bean, String... prepnames) {
		return this.map2JavaBean(m, bean, prepnames, prepnames);
	}


	public <T> T map2JavaBean(Map<?,?> m, T bean, String[] srcPrepnames, String[] prepnames) {

        Assert.notNull(m        , "map  == null");
        Assert.notNull(bean     , "bean == null");

		if ( CollectionUtil.argsIsBlank(prepnames) ) {
			prepnames = this.getPrepNames(bean);
		}
		if ( CollectionUtil.argsIsBlank(srcPrepnames) ) {
			srcPrepnames = prepnames;
		}

		this.beanCopy(m, bean, srcPrepnames, prepnames, mapVo, beanVo);
		return bean;
	}


	public <T> T map2JavaBean3(Map<?,?> m, T bean, String[] nkeys, String[] skeys, String[] dkeys) {

        Assert.notNull(m        , "map  == null");
        Assert.notNull(bean     , "bean == null");

		String[] prepnames = this.getPrepNames(bean);

		this.beanCopy(m, bean, prepnames, nkeys, skeys, dkeys, mapVo, beanVo);

		return bean;
	}


	public <T> T map2JavaBean(Map<?,?> m, Class<T> beanClazz) {
		return this.map2JavaBean(m, beanClazz, null, null);
	}


	public <T> T map2JavaBean(Map<?,?> m, Class<T> beanClazz, String... prepnames) {
		return this.map2JavaBean(m, beanClazz, prepnames, prepnames);
	}


	public <T> T map2JavaBean(Map<?,?> m, Class<T> beanClazz, String[] srcPrepnames, String[] prepnames) {

        Assert.notNull(m             , "map  == null");
        Assert.notNull(beanClazz     , "beanClazz == null");

		T bean = cerateBean(beanClazz);
	

		if ( CollectionUtil.argsIsBlank(prepnames) ) {
			prepnames = this.getPrepNames(bean);
		}
		if ( CollectionUtil.argsIsBlank(srcPrepnames) ) {
			srcPrepnames = prepnames;
		}

		this.map2JavaBean(m, bean, srcPrepnames, prepnames);

		return bean;

	}


	public <T> T map2JavaBean3(Map<?,?> m, Class<T> beanClazz, String[] nkeys, String[] skeys, String[] dkeys) {

        Assert.notNull(m             , "map  == null");
        Assert.notNull(beanClazz     , "beanClazz == null");

		T bean = cerateBean(beanClazz);

		String[] prepnames = this.getPrepNames(bean);

		this.beanCopy(m, bean, prepnames, nkeys, skeys, dkeys, mapVo, beanVo);

		return bean;
	}


	public <T> List<T> maps2JavaBeanList(Collection<?> ms, Class<T> beanClazz) {

        Assert.notNull(beanClazz     , "beanClazz == null");

		String[] prepnames;
		try {
			prepnames = this.getPrepNames(cerateBean(beanClazz));
		} catch (Exception e) {
			log.debug("mapList2JavaBeanList", e);
			throw ExceptionUtil.toRuntimeException(e);
		}

		return this.maps2JavaBeanList(ms, beanClazz, prepnames);
	}


	public <T> List<T> maps2JavaBeanList(Collection<?> ms, Class<T> beanClazz, String... prepnames) {
		return this.maps2JavaBeanList(ms, beanClazz, prepnames, prepnames);
	}


	public <T> List<T> maps2JavaBeanList(Collection<?> ms, Class<T> beanClazz, String[] srcPrepnames, String[] prepnames) {

		List<T> rl = new ArrayList<T>();

		if (ms != null && !ms.isEmpty()) {
			for (Object m : ms) {
				rl.add(this.map2JavaBean((Map<?,?>) m, beanClazz, srcPrepnames, prepnames));
			}
		}

		return rl;
	}

	public Map<String, Object> javaBean2Map(Object bean) {

        Assert.notNull(bean     , "bean == null");

		return this.javaBean2Map(bean, new HashMap<String, Object>(), this.getPrepNames(bean));
	}

	public Map<String, Object> javaBean2Map(Object bean, String... srcPrepnames) {
		return this.javaBean2Map(bean, new HashMap<String, Object>(), srcPrepnames, srcPrepnames);
	}

	public Map<String, Object> javaBean2Map(Object bean, String[] srcPrepnames, String[] prepnames) {
		return this.javaBean2Map(bean, new HashMap<String, Object>(), srcPrepnames, prepnames);
	}

	public <K, V> Map<K, V> javaBean2Map(Object bean, Map<K, V> m) {

        Assert.notNull(bean     , "bean == null");
        Assert.notNull(m        , "map == null");

		return this.javaBean2Map(bean, m, this.getPrepNames(bean));
	}

	public <K, V> Map<K, V> javaBean2Map(Object bean, Map<K, V> m, String... srcPrepnames) {
		return this.javaBean2Map(bean, m, srcPrepnames, srcPrepnames);
	}

	public <K, V> Map<K, V> javaBean2Map(Object bean, Map<K, V> m, String[] srcPrepnames, String[] prepnames) {

        Assert.notNull(bean     , "bean == null");
        Assert.notNull(m        , "map == null");

		if ( CollectionUtil.argsIsBlank(srcPrepnames) ) {
			srcPrepnames = this.getPrepNames(bean);
		}
		if ( CollectionUtil.argsIsBlank(prepnames) ) {
			prepnames = srcPrepnames;
		}
		this.beanCopy(bean, m, srcPrepnames, prepnames, beanVo, mapVo);

		return m;
	}

	public Map<String, Object> javaBean2Map1(Object bean, String... nkeys) {
		return this.javaBean2Map3(bean, new HashMap<String, Object>(), nkeys, null, null);
	}

	public Map<String, Object> javaBean2Map2(Object bean, String[] skeys, String[] dkeys) {
		return this.javaBean2Map3(bean, new HashMap<String, Object>(), null, skeys, dkeys);
	}

	public Map<String, Object> javaBean2Map3(Object bean, String[] nkeys, String[] skeys, String[] dkeys) {
		return this.javaBean2Map3(bean, new HashMap<String, Object>(), nkeys, skeys, dkeys);
	}

	public <K, V> Map<K, V> javaBean2Map1(Object bean, Map<K, V> m, String... nkeys) {
		return this.javaBean2Map3(bean, m, nkeys, null, null);
	}

	public <K, V> Map<K, V> javaBean2Map2(Object bean, Map<K, V> m, String[] skeys, String[] dkeys) {
		return this.javaBean2Map3(bean, m, null, skeys, dkeys);
	}

	public <K, V> Map<K, V> javaBean2Map3(Object bean, Map<K, V> m, String[] nkeys, String[] skeys, String[] dkeys) {

        Assert.notNull(bean     , "bean == null");
        Assert.notNull(m        , "map == null");

		String[] srcPrepnames = this.getPrepNames(bean);

		this.beanCopy(bean, m, srcPrepnames, nkeys, skeys, dkeys, beanVo, mapVo);

		return m;

	}

	public Map<String, Object> objectArray2Map(Object[] os, String... keys) {
		return this.objectArray2Map(os, keys, (String[]) null);
	}

	public Map<String, Object> objectArray2Map(Object[] os, String[] keys, String... nkeys) {

		Collection<String> bkl = null;
		if ( ! CollectionUtil.argsIsBlank(nkeys) ) {
			bkl = new HashSet<String>(Arrays.asList(nkeys));
		}

		Map<String, Object> m = new HashMap<String, Object>();

		for (int i = 0; i < keys.length; i++) {
			String k = keys[i];
			if ( bkl == null || !bkl.contains(k)) {
				this.mapVo.set(m, k, os[i]);
			}
		}

		return m;

	}

	public <T> T objectArray2JavaBean(Object[] os, T bean, String... keys) {
		return this.map2JavaBean(this.objectArray2Map(os, keys), bean);
	}

	public <T> T objectArray2JavaBean(Object[] os, T bean, String[] keys, String... nkeys) {
		return this.map2JavaBean(this.objectArray2Map(os, keys, nkeys), bean);
	}

	public <T> T objectArray2JavaBean(Object[] os, Class<T> beanClazz, String... keys) {
		return this.map2JavaBean(this.objectArray2Map(os, keys), beanClazz);
	}

	public <T> T objectArray2JavaBean(Object[] os, Class<T> beanClazz, String[] keys, String... nkeys) {
		return this.map2JavaBean(this.objectArray2Map(os, keys, nkeys), beanClazz);
	}

	public <T> List<T> objectArrays2JavaBeanList(Collection<Object[]> osl, Class<T> beanClazz, String... keys) {
		return this.objectArrays2JavaBeanList(osl, beanClazz, keys, (String[]) null);
	}

	public <T> List<T> objectArrays2JavaBeanList(Collection<Object[]> osl, Class<T> beanClazz, String[] keys, String... nkeys) {

		List<T> rl = new ArrayList<T>();

		if( osl != null ) for (Object[] os : osl) {
			rl.add(this.map2JavaBean(this.objectArray2Map(os, keys, nkeys), beanClazz));
		}

		return rl;
	}
	
	public <T> List<T> collections2JavaBeanList(Collection<? extends Collection<?>> osl, Class<T> beanClazz, String... keys) {
		return this.collections2JavaBeanList(osl, beanClazz, keys, (String[]) null);
	}

	public <T> List<T> collections2JavaBeanList(Collection<? extends Collection<?>> osl, Class<T> beanClazz, String[] keys, String... nkeys) {

		List<T> rl = new ArrayList<T>();

        if( osl != null ) for (Collection<?> os : osl) {
			rl.add(this.map2JavaBean(this.objectArray2Map(os.toArray(), keys, nkeys), beanClazz));
		}

		return rl;
	}
	
	
	
	
	
	
	public <S,T> List<T> objects2OtherList(Collection<S> ss,  BeanCallback<S,T> bc) {

		if( bc == null ) {
			throw new RuntimeException("bc == null");
		}
		
		List<T> rl = new ArrayList<T>();

		if (ss != null && !ss.isEmpty()) {
			for (S s : ss) {						
				T t = bc.source2Target( this, s );					
				rl.add(t);			
			}
		}

		return rl;
	}



}
