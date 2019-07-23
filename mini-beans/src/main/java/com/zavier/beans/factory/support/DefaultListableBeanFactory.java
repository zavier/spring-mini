package com.zavier.beans.factory.support;

import com.zavier.beans.BeansException;
import com.zavier.beans.factory.BeanFactory;
import com.zavier.beans.factory.NoSuchBeanDefinitionException;
import com.zavier.beans.factory.config.BeanDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultListableBeanFactory implements BeanFactory, BeanDefinitionRegistry {

    private BeanFactory parentBeanFactory;

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(64);

    /** Map of singleton and non-singleton bean names keyed by dependency type */
    private final Map<Class<?>, String[]> allBeanNamesByType = new ConcurrentHashMap<Class<?>, String[]>(64);

    /** Map of singleton-only bean names keyed by dependency type */
    private final Map<Class<?>, String[]> singletonBeanNamesByType = new ConcurrentHashMap<Class<?>, String[]>(64);

    /** List of bean definition names, in registration order */
    private final List<String> beanDefinitionNames = new ArrayList<String>();

    public DefaultListableBeanFactory() {
    }

    public DefaultListableBeanFactory(BeanFactory parentBeanFactory) {
        this.parentBeanFactory = parentBeanFactory;
    }

    @Override
    public Object getBean(String name) throws BeansException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsBean(String name) {
        return false;
    }

    @Override
    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isTypeMatch(String name, Class<?> targetType) throws NoSuchBeanDefinitionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        synchronized (this.beanDefinitionMap) {
            BeanDefinition oldBeanDefinition = this.beanDefinitionMap.get(beanName);
            if (oldBeanDefinition != null) {
                throw new BeansException("registerBeanDefinition error");
            } else {
                this.beanDefinitionNames.add(beanName);
            }
            this.beanDefinitionMap.put(beanName, beanDefinition);
        }
    }

    @Override
    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        beanDefinitionMap.remove(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionNames.contains(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionNames.toArray(new String[0]);
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitionMap.size();
    }
}
