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

    private volatile ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    /*====== delegate =====*/

    public String getId() {
        return applicationContext.getId();
    }

    public String getApplicationName() {
        return applicationContext.getApplicationName();
    }

    public String getDisplayName() {
        return applicationContext.getDisplayName();
    }

    public long getStartupDate() {
        return applicationContext.getStartupDate();
    }

    public ApplicationContext getParent() {
        return applicationContext.getParent();
    }

    public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
        return applicationContext.getAutowireCapableBeanFactory();
    }

    public Environment getEnvironment() {
        return applicationContext.getEnvironment();
    }

    public boolean containsBeanDefinition(String s) {
        return applicationContext.containsBeanDefinition(s);
    }

    public int getBeanDefinitionCount() {
        return applicationContext.getBeanDefinitionCount();
    }

    public String[] getBeanDefinitionNames() {
        return applicationContext.getBeanDefinitionNames();
    }

    public String[] getBeanNamesForType(Class<?> aClass) {
        return applicationContext.getBeanNamesForType(aClass);
    }

    public String[] getBeanNamesForType(Class<?> aClass, boolean b, boolean b1) {
        return applicationContext.getBeanNamesForType(aClass, b, b1);
    }

    public <T> Map<String, T> getBeansOfType(Class<T> aClass) throws BeansException {
        return applicationContext.getBeansOfType(aClass);
    }

    public <T> Map<String, T> getBeansOfType(Class<T> aClass, boolean b, boolean b1) throws BeansException {
        return applicationContext.getBeansOfType(aClass, b, b1);
    }

    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> aClass) throws BeansException {
        return applicationContext.getBeansWithAnnotation(aClass);
    }

    public <A extends Annotation> A findAnnotationOnBean(String s, Class<A> aClass) {
        return applicationContext.findAnnotationOnBean(s, aClass);
    }

    public Object getBean(String s) throws BeansException {
        return applicationContext.getBean(s);
    }

    public <T> T getBean(String s, Class<T> aClass) throws BeansException {
        return applicationContext.getBean(s, aClass);
    }

    public <T> T getBean(Class<T> aClass) throws BeansException {
        return applicationContext.getBean(aClass);
    }

    public Object getBean(String s, Object... objects) throws BeansException {
        return applicationContext.getBean(s, objects);
    }

    public boolean containsBean(String s) {
        return applicationContext.containsBean(s);
    }

    public boolean isSingleton(String s) throws NoSuchBeanDefinitionException {
        return applicationContext.isSingleton(s);
    }

    public boolean isPrototype(String s) throws NoSuchBeanDefinitionException {
        return applicationContext.isPrototype(s);
    }

    public boolean isTypeMatch(String s, Class<?> aClass) throws NoSuchBeanDefinitionException {
        return applicationContext.isTypeMatch(s, aClass);
    }

    public Class<?> getType(String s) throws NoSuchBeanDefinitionException {
        return applicationContext.getType(s);
    }

    public String[] getAliases(String s) {
        return applicationContext.getAliases(s);
    }

    public BeanFactory getParentBeanFactory() {
        return applicationContext.getParentBeanFactory();
    }

    public boolean containsLocalBean(String s) {
        return applicationContext.containsLocalBean(s);
    }

    public String getMessage(String s, Object[] objects, String s1, Locale locale) {
        return applicationContext.getMessage(s, objects, s1, locale);
    }

    public String getMessage(String s, Object[] objects, Locale locale) throws NoSuchMessageException {
        return applicationContext.getMessage(s, objects, locale);
    }

    public String getMessage(MessageSourceResolvable messageSourceResolvable, Locale locale) throws NoSuchMessageException {
        return applicationContext.getMessage(messageSourceResolvable, locale);
    }

    public void publishEvent(ApplicationEvent applicationEvent) {
        applicationContext.publishEvent(applicationEvent);
    }

    public Resource[] getResources(String s) throws IOException {
        return applicationContext.getResources(s);
    }

    public Resource getResource(String s) {
        return applicationContext.getResource(s);
    }

    public ClassLoader getClassLoader() {
        return applicationContext.getClassLoader();
    }
}
