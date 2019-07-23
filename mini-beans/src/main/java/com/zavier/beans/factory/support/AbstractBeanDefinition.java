package com.zavier.beans.factory.support;

import com.zavier.beans.factory.config.AutowireCapableBeanFactory;
import com.zavier.beans.factory.config.BeanDefinition;
import com.zavier.core.io.Resource;
import com.zavier.util.ClassUtils;

public abstract class AbstractBeanDefinition implements BeanDefinition {
    /**
     * Constant for the default scope name: "", equivalent to singleton status
     * but to be overridden from a parent bean definition (if applicable).
     */
    public static final String SCOPE_DEFAULT = "";

    /**
     * Constant that indicates no autowiring at all.
     * @see #setAutowireMode
     */
    public static final int AUTOWIRE_NO = AutowireCapableBeanFactory.AUTOWIRE_NO;

    /**
     * Constant that indicates autowiring bean properties by name.
     * @see #setAutowireMode
     */
    public static final int AUTOWIRE_BY_NAME = AutowireCapableBeanFactory.AUTOWIRE_BY_NAME;

    /**
     * Constant that indicates autowiring bean properties by type.
     * @see #setAutowireMode
     */
    public static final int AUTOWIRE_BY_TYPE = AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE;

    /**
     * Constant that indicates autowiring a constructor.
     * @see #setAutowireMode
     */
    public static final int AUTOWIRE_CONSTRUCTOR = AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR;

    /**
     * Constant that indicates no dependency check at all.
     * @see #setDependencyCheck
     */
    public static final int DEPENDENCY_CHECK_NONE = 0;

    /**
     * Constant that indicates dependency checking for object references.
     * @see #setDependencyCheck
     */
    public static final int DEPENDENCY_CHECK_OBJECTS = 1;

    /**
     * Constant that indicates dependency checking for "simple" properties.
     * @see #setDependencyCheck
     */
    public static final int DEPENDENCY_CHECK_SIMPLE = 2;

    /**
     * Constant that indicates dependency checking for all properties
     * (object references as well as "simple" properties).
     * @see #setDependencyCheck
     */
    public static final int DEPENDENCY_CHECK_ALL = 3;

    /**
     * Constant that indicates the container should attempt to infer the
     * {@link #setDestroyMethodName destroy method name} for a bean as opposed to
     * explicit specification of a method name. The value {@value} is specifically
     * designed to include characters otherwise illegal in a method name, ensuring
     * no possibility of collisions with legitimately named methods having the same
     * name.
     * <p>Currently, the method names detected during destroy method inference
     * are "close" and "shutdown", if present on the specific bean class.
     */
    public static final String INFER_METHOD = "(inferred)";


    private volatile Object beanClass;

    private String scope = SCOPE_DEFAULT;

    private boolean abstractFlag = false;

    private boolean lazyInit = false;

    private int autowireMode = AUTOWIRE_NO;

    private int dependencyCheck = DEPENDENCY_CHECK_NONE;

    private String[] dependsOn;

    private boolean autowireCandidate = true;

    private boolean primary = false;

    private String factoryBeanName;

    private String factoryMethodName;

    private String initMethodName;

    private String destroyMethodName;

    private boolean enforceInitMethod = true;

    private boolean enforceDestroyMethod = true;

    private boolean synthetic = false;


    private String description;

    private Resource resource;

    /**
     * Return whether this definition specifies a bean class.
     */
    public boolean hasBeanClass() {
        return (this.beanClass instanceof Class);
    }

    /**
     * Specify the class for this bean.
     */
    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * Return the class of the wrapped bean, if already resolved.
     * @return the bean class, or {@code null} if none defined
     * @throws IllegalStateException if the bean definition does not define a bean class,
     * or a specified bean class name has not been resolved into an actual Class
     */
    public Class<?> getBeanClass() throws IllegalStateException {
        Object beanClassObject = this.beanClass;
        if (beanClassObject == null) {
            throw new IllegalStateException("No bean class specified on bean definition");
        }
        if (!(beanClassObject instanceof Class)) {
            throw new IllegalStateException(
                    "Bean class name [" + beanClassObject + "] has not been resolved into an actual Class");
        }
        return (Class<?>) beanClassObject;
    }

    @Override
    public void setBeanClassName(String beanClassName) {
        this.beanClass = beanClassName;
    }

    @Override
    public String getBeanClassName() {
        Object beanClassObject = this.beanClass;
        if (beanClassObject instanceof Class) {
            return ((Class<?>) beanClassObject).getName();
        } else {
            return (String) beanClassObject;
        }
    }

    /**
     * Determine the class of the wrapped bean, resolving it from a
     * specified class name if necessary. Will also reload a specified
     * Class from its name when called with the bean class already resolved.
     * @param classLoader the ClassLoader to use for resolving a (potential) class name
     * @return the resolved bean class
     * @throws ClassNotFoundException if the class name could be resolved
     */
    public Class<?> resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException {
        String className = getBeanClassName();
        if (className == null) {
            return null;
        }
        Class<?> resolvedClass = ClassUtils.forName(className, classLoader);
        this.beanClass = resolvedClass;
        return resolvedClass;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * Return the name of the target scope for the bean.
     */
    @Override
    public String getScope() {
        return this.scope;
    }

    @Override
    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope);
    }

    @Override
    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(scope);
    }


    /**
     * Return whether this bean is "abstract", i.e. not meant to be instantiated
     * itself but rather just serving as parent for concrete child bean definitions.
     */
    @Override
    public boolean isAbstract() {
        return this.abstractFlag;
    }

    /**
     * Set whether this bean should be lazily initialized.
     * <p>If {@code false}, the bean will get instantiated on startup by bean
     * factories that perform eager initialization of singletons.
     */
    @Override
    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    /**
     * Return whether this bean should be lazily initialized, i.e. not
     * eagerly instantiated on startup. Only applicable to a singleton bean.
     */
    @Override
    public boolean isLazyInit() {
        return this.lazyInit;
    }

    public void setAutowireMode(int autowireMode) {
        this.autowireMode = autowireMode;
    }

    /**
     * Return the autowire mode as specified in the bean definition.
     */
    public int getAutowireMode() {
        return this.autowireMode;
    }

    /**
     * Set the dependency check code.
     * @param dependencyCheck the code to set.
     * Must be one of the four constants defined in this class.
     * @see #DEPENDENCY_CHECK_NONE
     * @see #DEPENDENCY_CHECK_OBJECTS
     * @see #DEPENDENCY_CHECK_SIMPLE
     * @see #DEPENDENCY_CHECK_ALL
     */
    public void setDependencyCheck(int dependencyCheck) {
        this.dependencyCheck = dependencyCheck;
    }

    /**
     * Return the dependency check code.
     */
    public int getDependencyCheck() {
        return this.dependencyCheck;
    }

    /**
     * Set the names of the beans that this bean depends on being initialized.
     * The bean factory will guarantee that these beans get initialized first.
     * <p>Note that dependencies are normally expressed through bean properties or
     * constructor arguments. This property should just be necessary for other kinds
     * of dependencies like statics (*ugh*) or database preparation on startup.
     */
    @Override
    public void setDependsOn(String[] dependsOn) {
        this.dependsOn = dependsOn;
    }

    /**
     * Return the bean names that this bean depends on.
     */
    @Override
    public String[] getDependsOn() {
        return this.dependsOn;
    }

    /**
     * Set whether this bean is a candidate for getting autowired into some other bean.
     */
    @Override
    public void setAutowireCandidate(boolean autowireCandidate) {
        this.autowireCandidate = autowireCandidate;
    }

    /**
     * Return whether this bean is a candidate for getting autowired into some other bean.
     */
    @Override
    public boolean isAutowireCandidate() {
        return this.autowireCandidate;
    }

    /**
     * Set whether this bean is a primary autowire candidate.
     * If this value is true for exactly one bean among multiple
     * matching candidates, it will serve as a tie-breaker.
     */
    @Override
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    /**
     * Return whether this bean is a primary autowire candidate.
     * If this value is true for exactly one bean among multiple
     * matching candidates, it will serve as a tie-breaker.
     */
    @Override
    public boolean isPrimary() {
        return this.primary;
    }

    @Override
    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    @Override
    public String getFactoryBeanName() {
        return this.factoryBeanName;
    }

    @Override
    public void setFactoryMethodName(String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }

    @Override
    public String getFactoryMethodName() {
        return this.factoryMethodName;
    }

    /**
     * Set the name of the initializer method. The default is {@code null}
     * in which case there is no initializer method.
     */
    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    /**
     * Return the name of the initializer method.
     */
    public String getInitMethodName() {
        return this.initMethodName;
    }

    /**
     * Specify whether or not the configured init method is the default.
     * Default value is {@code false}.
     * @see #setInitMethodName
     */
    public void setEnforceInitMethod(boolean enforceInitMethod) {
        this.enforceInitMethod = enforceInitMethod;
    }

    /**
     * Indicate whether the configured init method is the default.
     * @see #getInitMethodName()
     */
    public boolean isEnforceInitMethod() {
        return this.enforceInitMethod;
    }

    /**
     * Set the name of the destroy method. The default is {@code null}
     * in which case there is no destroy method.
     */
    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    /**
     * Return the name of the destroy method.
     */
    public String getDestroyMethodName() {
        return this.destroyMethodName;
    }

    /**
     * Specify whether or not the configured destroy method is the default.
     * Default value is {@code false}.
     * @see #setDestroyMethodName
     */
    public void setEnforceDestroyMethod(boolean enforceDestroyMethod) {
        this.enforceDestroyMethod = enforceDestroyMethod;
    }

    /**
     * Indicate whether the configured destroy method is the default.
     * @see #getDestroyMethodName
     */
    public boolean isEnforceDestroyMethod() {
        return this.enforceDestroyMethod;
    }


    /**
     * Set whether this bean definition is 'synthetic', that is, not defined
     * by the application itself (for example, an infrastructure bean such
     * as a helper for auto-proxying, created through {@code &ltaop:config&gt;}).
     */
    public void setSynthetic(boolean synthetic) {
        this.synthetic = synthetic;
    }

    /**
     * Return whether this bean definition is 'synthetic', that is,
     * not defined by the application itself.
     */
    public boolean isSynthetic() {
        return this.synthetic;
    }

}
