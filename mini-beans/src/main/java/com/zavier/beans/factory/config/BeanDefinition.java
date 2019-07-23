package com.zavier.beans.factory.config;

public interface BeanDefinition {

    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";

    int ROLE_APPLICATION = 0;

    int ROLE_SUPPORT = 1;

    int ROLE_INFRASTRUCTURE = 2;


    /**
     * Return the name of the parent definition of this bean definition, if any.
     */
    String getParentName();

    /**
     * Set the name of the parent definition of this bean definition, if any.
     */
    void setParentName(String parentName);

    /**
     * Return the current bean class name of this bean definition.
     * <p>Note that this does not have to be the actual class name used at runtime, in
     * case of a child definition overriding/inheriting the class name from its parent.
     * Hence, do <i>not</i> consider this to be the definitive bean type at runtime but
     * rather only use it for parsing purposes at the individual bean definition level.
     */
    String getBeanClassName();

    /**
     * Override the bean class name of this bean definition.
     * <p>The class name can be modified during bean factory post-processing,
     * typically replacing the original class name with a parsed variant of it.
     */
    void setBeanClassName(String beanClassName);

    /**
     * Return the factory bean name, if any.
     */
    String getFactoryBeanName();

    /**
     * Specify the factory bean to use, if any.
     */
    void setFactoryBeanName(String factoryBeanName);

    /**
     * Return a factory method, if any.
     */
    String getFactoryMethodName();

    /**
     * Specify a factory method, if any. This method will be invoked with
     * constructor arguments, or with no arguments if none are specified.
     * The method will be invoked on the specified factory bean, if any,
     * or otherwise as a static method on the local bean class.
     * @param factoryMethodName static factory method name,
     * or {@code null} if normal constructor creation should be used
     * @see #getBeanClassName()
     */
    void setFactoryMethodName(String factoryMethodName);

    /**
     * Return the name of the current target scope for this bean,
     * or {@code null} if not known yet.
     */
    String getScope();

    void setScope(String scope);

    /**
     * Return whether this bean should be lazily initialized, i.e. not
     * eagerly instantiated on startup. Only applicable to a singleton bean.
     */
    boolean isLazyInit();

    /**
     * Set whether this bean should be lazily initialized.
     * <p>If {@code false}, the bean will get instantiated on startup by bean
     * factories that perform eager initialization of singletons.
     */
    void setLazyInit(boolean lazyInit);

    /**
     * Return the bean names that this bean depends on.
     */
    String[] getDependsOn();

    /**
     * Set the names of the beans that this bean depends on being initialized.
     * The bean factory will guarantee that these beans get initialized first.
     */
    void setDependsOn(String[] dependsOn);

    /**
     * Return whether this bean is a candidate for getting autowired into some other bean.
     */
    boolean isAutowireCandidate();

    /**
     * Set whether this bean is a candidate for getting autowired into some other bean.
     */
    void setAutowireCandidate(boolean autowireCandidate);

    /**
     * Return whether this bean is a primary autowire candidate.
     * If this value is true for exactly one bean among multiple
     * matching candidates, it will serve as a tie-breaker.
     */
    boolean isPrimary();

    /**
     * Set whether this bean is a primary autowire candidate.
     * <p>If this value is true for exactly one bean among multiple
     * matching candidates, it will serve as a tie-breaker.
     */
    void setPrimary(boolean primary);

    boolean isSingleton();

    boolean isPrototype();

    /**
     * Return whether this bean is "abstract", that is, not meant to be instantiated.
     */
    boolean isAbstract();

}
