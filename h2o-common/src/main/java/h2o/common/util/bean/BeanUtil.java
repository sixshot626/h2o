package h2o.common.util.bean;

import h2o.common.collection.IgnoreCaseMap;
import h2o.common.lang.tuple.Entry;
import h2o.common.thirdparty.spring.util.Assert;
import h2o.common.util.bean.support.DefaultBeanPropertyInfoImpl;
import h2o.common.util.bean.support.JoddBeanDescriptorImpl;
import h2o.common.util.bean.support.JoddBeanUtilVOImpl;
import h2o.common.util.bean.support.MapVOImpl;
import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.collection.ListBuilder;
import h2o.common.util.collection.MapBuilder;
import h2o.common.util.lang.InstanceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public final class BeanUtil {

    private static final Logger log = LoggerFactory.getLogger( BeanUtil.class.getName() );

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
        return new BeanUtil( new Builder(this).setBeanVo( beanVo ) );
	}

	public BeanUtil mapVo( ValOperate mapVo ) {
        return new BeanUtil( new Builder(this).setMapVo( mapVo ) );
	}





	public String[] analysePrepNames(Object bean) {
		return this.beanDescriptor.getPrepNames(bean);
	}


	private static final class PrepName {

	    private String[] srcPrepNames;

	    private String[] targetPrepNames;

        private String[] skipPrepNames;

        private List<Entry> mapPrepNames;


        public PrepName(String[] srcPrepNames, String[] targetPrepNames) {
            this.srcPrepNames = srcPrepNames;
            this.targetPrepNames = targetPrepNames;
        }

        public PrepName skip(String[] skipKeys) {
            this.skipPrepNames = skipKeys;
            return this;
        }

        public PrepName map( String[] srcKeys, String[] targetKeys) {

            if ( !CollectionUtil.argsIsBlank(targetKeys) ) {

                if ( CollectionUtil.argsIsBlank( srcKeys ) ) {
                    srcKeys = targetKeys;
                } else if ( srcKeys.length != targetKeys.length ) {
                    throw new IllegalArgumentException( "src.length != target.length" );
                }

                List<Entry> m = ListBuilder.newList();

                for ( int i = 0 ; i < srcKeys.length ; i++ ) {
                    m.add( new Entry( srcKeys[i] , targetKeys[i] ) );
                }

                this.mapPrepNames = m;
            }


            return this;
        }

        public List<Entry<String,String>> proc(boolean ignoreCase ) {


            Map<String,String> srcKeyMap = ppMap( srcPrepNames, ignoreCase );
            Map<String,String> targetKeyMap = ppMap( targetPrepNames, ignoreCase );

            Map<String,String> skipKeyMap = ppMap( skipPrepNames , ignoreCase );

            List<Entry<String,String>> result = ListBuilder.newList();

            if ( mapPrepNames != null ) {

                for ( Entry m : mapPrepNames ) {

                    if ( !skipKeyMap.containsKey( m.key ) ) {

                        String src = srcKeyMap.get( m.getKey() );
                        String target = targetKeyMap.get( m.value );
                        if ( src != null && target != null ) {
                            result.add( new Entry( src,target ) );
                        }

                    }

                }

            } else {

                for ( String target : targetPrepNames ) {

                    if ( !skipKeyMap.containsKey( target ) ) {

                        String src = srcKeyMap.get( target );
                        if ( src != null ) {
                            result.add( new Entry( src,target ) );
                        }

                    }

                }

            }

            return result;

        }


        private Map<String,String> ppMap( String[] ps , boolean  ignoreCase ) {

            if ( CollectionUtil.argsIsBlank(ps) ) {
                return MapBuilder.newEmptyMap();
            }

            Map<String,String> map = MapBuilder.newMap( ps.length );
            if ( ignoreCase ) {
                map = new IgnoreCaseMap<String>( map );
            }

            for ( String p : ps ) {
                map.put( p , p );
            }

            return map;
        }

    }




    public <T> T beanCopy( Object srcBean, T bean, List<Entry<String,String>> prepNames ) {

	    ValOperate svo = srcBean instanceof Map ? mapVo : beanVo;
        ValOperate vo  = bean instanceof Map ? mapVo : beanVo;

        return beanCopy( srcBean, bean, prepNames, svo, vo , this.cover , this.procNull ,  this.beanPropertyInfo );

	}


	private static <T> T beanCopy(Object srcBean, T bean, List<Entry<String,String> > prepNames, ValOperate svo, ValOperate vo , boolean cover , boolean procNull , BeanPropertyInfo beanPropertyInfo ) {

		for ( Entry<String,String> pp : prepNames ) {

			String spn = pp.key;
			String pn = pp.value;

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


    public <T> T beanCopy( Object srcBean, Class<T> beanClazz ) {
        return this.beanCopy3(srcBean, createBean(beanClazz) , null , null , null );
    }

    public <T> T beanCopy(Object srcBean, Class<T> beanClazz , String... prepNames) {
        return this.beanCopy3(srcBean, createBean(beanClazz) , null , prepNames , prepNames );
    }

    public <T> T beanCopy(Object srcBean, Class<T> beanClazz , String[] srcPrepNames, String[] prepNames ) {
        return this.beanCopy3( srcBean, createBean(beanClazz), null , srcPrepNames, prepNames );
    }

    public <T> T beanCopySkip(Object srcBean, Class<T> beanClazz , String[] skipPrepNames ) {
        return this.beanCopy3(srcBean, createBean(beanClazz),  skipPrepNames, null , null );
    }




    private <T> T createBean(Class<T> beanClazz  ) {
	    if ( beanClazz.getClass().equals( Map.class ) ) {
            return (T)new HashMap();
        }

	    return InstanceUtil.newInstance( beanClazz );
    }



	public <T> T beanCopy( Object srcBean, T bean) {
		return this.beanCopy3(srcBean, bean , null , null , null );
	}

	public <T> T beanCopy(Object srcBean, T bean, String... prepNames) {
		return this.beanCopy3(srcBean, bean, null, prepNames, prepNames);
	}

	public <T> T beanCopy(Object srcBean, T bean, String[] srcPrepNames, String[] prepNames ) {
		return this.beanCopy3( srcBean, bean, null , srcPrepNames, prepNames );
	}

	public <T> T beanCopySkip(Object srcBean, T bean, String[] skipPrepNames) {
		return this.beanCopy3(srcBean, bean , skipPrepNames, null , null );
	}



    private  <T> T beanCopy3( Object srcBean, T bean , String[] skipKeys , String[] srcKeys , String[] targetKeys ) {

	    Assert.notNull(srcBean   , "srcBean == null");
        Assert.notNull(bean      , "bean == null");

        String[] srcPrepNames = srcBean instanceof Map ? mapKeys( (Map)srcBean ) : this.analysePrepNames(srcBean);
        String[] targetPrepNames = bean instanceof Map ? srcPrepNames : this.analysePrepNames(bean);

        PrepName pn = new PrepName( srcPrepNames , targetPrepNames ).skip(skipKeys).map(srcKeys, targetKeys);

        return this.beanCopy( srcBean , bean , pn.proc( this.ignoreCase ) );

    }


    private String[] mapKeys( Map m ) {
	    Set<String> keys = m.keySet();
	    return keys.toArray( new String[ keys.size() ] );
    }





	public Map<String, Object> bean2Map(Object bean) {
        return this.beanCopy3(bean, new HashMap<String, Object>() , null , null , null );
	}

	public Map<String, Object> bean2Map(Object bean, String... prepNames) {
        return this.beanCopy3(bean, new HashMap<String, Object>() , null , prepNames , prepNames );
	}

	public Map<String, Object> bean2Map(Object bean, String[] srcPrepNames, String[] prepNames) {
        return this.beanCopy3(bean, new HashMap<String, Object>() , null , srcPrepNames , prepNames );
	}

    public Map<String, Object> bean2MapSkip(Object bean, String[] skipPrepNames) {
        return this.beanCopy3(bean, new HashMap<String, Object>() , skipPrepNames , null , null );
    }





}
