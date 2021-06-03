package h2o.common.thirdparty.spring.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Map;

@Component
public class SpringContext implements ApplicationContextAware {

    private static volatile ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        ApplicationContext context = applicationContext;
        if ( context == null ) {
            throw new IllegalStateException("ApplicationContext not initialized");
        }
        return context;
    }

    /*====== delegate =====*/

    public static String getId() {
        return getApplicationContext().getId();
    }

    public static String getApplicationName() {
        return getApplicationContext().getApplicationName();
    }

    public static String getDisplayName() {
        return getApplicationContext().getDisplayName();
    }

    public static long getStartupDate() {
        return getApplicationContext().getStartupDate();
    }

    public static ApplicationContext getParent() {
        return getApplicationContext().getParent();
    }

    public static AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
        return getApplicationContext().getAutowireCapableBeanFactory();
    }

    public static Environment getEnvironment() {
        return getApplicationContext().getEnvironment();
    }

    public static boolean containsBeanDefinition(String s) {
        return getApplicationContext().containsBeanDefinition(s);
    }

    public static int getBeanDefinitionCount() {
        return getApplicationContext().getBeanDefinitionCount();
    }

    public static String[] getBeanDefinitionNames() {
        return getApplicationContext().getBeanDefinitionNames();
    }

    public static String[] getBeanNamesForType(Class<?> aClass) {
        return getApplicationContext().getBeanNamesForType(aClass);
    }

    public static String[] getBeanNamesForType(Class<?> aClass, boolean b, boolean b1) {
        return getApplicationContext().getBeanNamesForType(aClass, b, b1);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> aClass) throws BeansException {
        return getApplicationContext().getBeansOfType(aClass);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> aClass, boolean b, boolean b1) throws BeansException {
        return getApplicationContext().getBeansOfType(aClass, b, b1);
    }

    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> aClass) throws BeansException {
        return getApplicationContext().getBeansWithAnnotation(aClass);
    }

    public static <A extends Annotation> A findAnnotationOnBean(String s, Class<A> aClass) {
        return getApplicationContext().findAnnotationOnBean(s, aClass);
    }

    public static Object getBean(String s) throws BeansException {
        return getApplicationContext().getBean(s);
    }

    public static <T> T getBean(String s, Class<T> aClass) throws BeansException {
        return getApplicationContext().getBean(s, aClass);
    }

    public static <T> T getBean(Class<T> aClass) throws BeansException {
        return getApplicationContext().getBean(aClass);
    }

    public static Object getBean(String s, Object... objects) throws BeansException {
        return getApplicationContext().getBean(s, objects);
    }

    public static boolean containsBean(String s) {
        return getApplicationContext().containsBean(s);
    }

    public static boolean isSingleton(String s) throws NoSuchBeanDefinitionException {
        return getApplicationContext().isSingleton(s);
    }

    public static boolean isPrototype(String s) throws NoSuchBeanDefinitionException {
        return getApplicationContext().isPrototype(s);
    }

    public static boolean isTypeMatch(String s, Class<?> aClass) throws NoSuchBeanDefinitionException {
        return getApplicationContext().isTypeMatch(s, aClass);
    }

    public static Class<?> getType(String s) throws NoSuchBeanDefinitionException {
        return getApplicationContext().getType(s);
    }

    public static String[] getAliases(String s) {
        return getApplicationContext().getAliases(s);
    }

    public static BeanFactory getParentBeanFactory() {
        return getApplicationContext().getParentBeanFactory();
    }

    public static boolean containsLocalBean(String s) {
        return getApplicationContext().containsLocalBean(s);
    }

    public static String getMessage(String s, Object[] objects, String s1, Locale locale) {
        return getApplicationContext().getMessage(s, objects, s1, locale);
    }

    public static String getMessage(String s, Object[] objects, Locale locale) throws NoSuchMessageException {
        return getApplicationContext().getMessage(s, objects, locale);
    }

    public static String getMessage(MessageSourceResolvable messageSourceResolvable, Locale locale) throws NoSuchMessageException {
        return getApplicationContext().getMessage(messageSourceResolvable, locale);
    }

    public static void publishEvent(ApplicationEvent applicationEvent) {
        getApplicationContext().publishEvent(applicationEvent);
    }

    public static Resource[] getResources(String s) throws IOException {
        return getApplicationContext().getResources(s);
    }

    public static Resource getResource(String s) {
        return getApplicationContext().getResource(s);
    }

    public static ClassLoader getClassLoader() {
        return getApplicationContext().getClassLoader();
    }
}
