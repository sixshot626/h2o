package h2o.common.thirdparty.spring.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;

@Component
public class SpringFactory implements BeanFactoryAware {
	
	private static final Lock lock = new java.util.concurrent.locks.ReentrantLock();

	private static volatile BeanFactory beanFactory;

	public  static void setStaticConfigPath(String... path) {
		lock.lock();
		try {
			beanFactory = new ClassPathXmlApplicationContext(path);
		} finally {
			lock.unlock();
		}	
	}
	
	public void setConfigPath(String... path) {
		setStaticConfigPath(path);
	}

    public void setBeanFactory(BeanFactory bf) throws BeansException {
        lock.lock();
        try {
            beanFactory = bf;
        } finally {
            lock.unlock();
        }
    }

    public static BeanFactory getBeanFactory()  {
        BeanFactory bf = beanFactory;
        if ( bf == null ) {
            throw new IllegalStateException("BeanFactory not initialized");
        }
        return bf;
    }


	@SuppressWarnings("unchecked")
	public static <T> T getObject(String beanId) {
		
		return (T) getBeanFactory().getBean(beanId);
	}


	/*====== delegate =====*/


    public static Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(requiredType);
    }

    public static Object getBean(String name, Object... args) throws BeansException {
        return getBeanFactory().getBean(name, args);
    }


    public static boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return getBeanFactory().isSingleton(name);
    }

    public static boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
        return getBeanFactory().isPrototype(name);
    }

    public static boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
        return getBeanFactory().isTypeMatch(name, typeToMatch);
    }

    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return getBeanFactory().getType(name);
    }

    public static String[] getAliases(String name) {
        return getBeanFactory().getAliases(name);
    }



}
