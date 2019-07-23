package com.zavier.beans.factory.xml;

import com.zavier.beans.BeansException;
import com.zavier.beans.factory.config.BeanDefinition;
import com.zavier.beans.factory.config.BeanDefinitionHolder;
import com.zavier.beans.factory.support.AbstractBeanDefinition;
import com.zavier.beans.factory.support.BeanDefinitionRegistry;
import com.zavier.beans.factory.support.GenericBeanDefinition;
import com.zavier.util.ClassUtils;
import com.zavier.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

public class DefaultBeanDefinitionDocumentReader implements BeanDefinitionDocumentReader {

    private static final Logger logger = LoggerFactory.getLogger(DefaultBeanDefinitionDocumentReader.class);

    public static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";

    public static final String MULTI_VALUE_ATTRIBUTE_DELIMITERS = ",; ";

    /**
     * Value of a T/F attribute that represents true.
     * Anything else represents false. Case seNsItive.
     */
    public static final String TRUE_VALUE = "true";

    public static final String FALSE_VALUE = "false";

    public static final String DEFAULT_VALUE = "default";

    public static final String DESCRIPTION_ELEMENT = "description";

    public static final String AUTOWIRE_NO_VALUE = "no";

    public static final String AUTOWIRE_BY_NAME_VALUE = "byName";

    public static final String AUTOWIRE_BY_TYPE_VALUE = "byType";

    public static final String AUTOWIRE_CONSTRUCTOR_VALUE = "constructor";

    public static final String AUTOWIRE_AUTODETECT_VALUE = "autodetect";

    public static final String DEPENDENCY_CHECK_ALL_ATTRIBUTE_VALUE = "all";

    public static final String DEPENDENCY_CHECK_SIMPLE_ATTRIBUTE_VALUE = "simple";

    public static final String DEPENDENCY_CHECK_OBJECTS_ATTRIBUTE_VALUE = "objects";

    public static final String NAME_ATTRIBUTE = "name";

    public static final String BEAN_ELEMENT = "bean";

    public static final String META_ELEMENT = "meta";

    public static final String ID_ATTRIBUTE = "id";

    public static final String PARENT_ATTRIBUTE = "parent";

    public static final String CLASS_ATTRIBUTE = "class";

    public static final String ABSTRACT_ATTRIBUTE = "abstract";

    public static final String SCOPE_ATTRIBUTE = "scope";

    public static final String LAZY_INIT_ATTRIBUTE = "lazy-init";

    public static final String AUTOWIRE_ATTRIBUTE = "autowire";

    public static final String AUTOWIRE_CANDIDATE_ATTRIBUTE = "autowire-candidate";

    public static final String PRIMARY_ATTRIBUTE = "primary";

    public static final String DEPENDENCY_CHECK_ATTRIBUTE = "dependency-check";

    public static final String DEPENDS_ON_ATTRIBUTE = "depends-on";

    public static final String INIT_METHOD_ATTRIBUTE = "init-method";

    public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";

    public static final String FACTORY_METHOD_ATTRIBUTE = "factory-method";

    public static final String FACTORY_BEAN_ATTRIBUTE = "factory-bean";

    public static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg";

    public static final String INDEX_ATTRIBUTE = "index";

    public static final String TYPE_ATTRIBUTE = "type";

    public static final String VALUE_TYPE_ATTRIBUTE = "value-type";

    public static final String KEY_TYPE_ATTRIBUTE = "key-type";

    public static final String PROPERTY_ELEMENT = "property";

    public static final String REF_ATTRIBUTE = "ref";

    public static final String VALUE_ATTRIBUTE = "value";

    public static final String LOOKUP_METHOD_ELEMENT = "lookup-method";

    public static final String REPLACED_METHOD_ELEMENT = "replaced-method";

    public static final String REPLACER_ATTRIBUTE = "replacer";

    public static final String ARG_TYPE_ELEMENT = "arg-type";

    public static final String ARG_TYPE_MATCH_ATTRIBUTE = "match";

    public static final String REF_ELEMENT = "ref";

    public static final String IDREF_ELEMENT = "idref";

    public static final String BEAN_REF_ATTRIBUTE = "bean";

    public static final String LOCAL_REF_ATTRIBUTE = "local";

    public static final String PARENT_REF_ATTRIBUTE = "parent";

    public static final String VALUE_ELEMENT = "value";

    public static final String NULL_ELEMENT = "null";

    public static final String ARRAY_ELEMENT = "array";

    public static final String LIST_ELEMENT = "list";

    public static final String SET_ELEMENT = "set";

    public static final String MAP_ELEMENT = "map";

    public static final String ENTRY_ELEMENT = "entry";

    public static final String KEY_ELEMENT = "key";

    public static final String KEY_ATTRIBUTE = "key";

    public static final String KEY_REF_ATTRIBUTE = "key-ref";

    public static final String VALUE_REF_ATTRIBUTE = "value-ref";

    public static final String PROPS_ELEMENT = "props";

    public static final String PROP_ELEMENT = "prop";

    public static final String MERGE_ATTRIBUTE = "merge";

    public static final String QUALIFIER_ELEMENT = "qualifier";

    public static final String QUALIFIER_ATTRIBUTE_ELEMENT = "attribute";

    public static final String DEFAULT_LAZY_INIT_ATTRIBUTE = "default-lazy-init";

    public static final String DEFAULT_MERGE_ATTRIBUTE = "default-merge";

    public static final String DEFAULT_AUTOWIRE_ATTRIBUTE = "default-autowire";

    public static final String DEFAULT_DEPENDENCY_CHECK_ATTRIBUTE = "default-dependency-check";

    public static final String DEFAULT_AUTOWIRE_CANDIDATES_ATTRIBUTE = "default-autowire-candidates";

    public static final String DEFAULT_INIT_METHOD_ATTRIBUTE = "default-init-method";

    public static final String DEFAULT_DESTROY_METHOD_ATTRIBUTE = "default-destroy-method";

    //=======================================================

    public static final String NESTED_BEANS_ELEMENT = "beans";

    public static final String ALIAS_ELEMENT = "alias";

    public static final String ALIAS_ATTRIBUTE = "alias";

    public static final String IMPORT_ELEMENT = "import";

    public static final String RESOURCE_ATTRIBUTE = "resource";

    public static final String PROFILE_ATTRIBUTE = "profile";


    private final Set<String> usedNames = new HashSet<String>();



    private BeanDefinitionRegistry beanDefinitionRegistry;

    @Override
    public void registerBeanDefinitions(Document doc, BeanDefinitionRegistry beanDefinitionRegistry) {
        Element root = doc.getDocumentElement();
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        doRegisterBeanDefinitions(root);
    }

    protected void doRegisterBeanDefinitions(Element root) {
        parseBeanDefinitions(root);
    }

    protected void parseBeanDefinitions(Element root) {
        if (isDefaultNamespace(root)) {
            NodeList nl = root.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                if (node instanceof Element) {
                    Element ele = (Element) node;
                    if (isDefaultNamespace(ele)) {
                        parseDefaultElement(ele);
                    }
                    else {
//                        todo parseCustomElement(ele);
                    }
                }
            }
        }
        else {
//            todo parseCustomElement(root);
        }
    }

    private void parseDefaultElement(Element ele) {
        if (nodeNameEquals(ele, IMPORT_ELEMENT)) {
            //todo importBeanDefinitionResource(ele);
        }
        else if (nodeNameEquals(ele, ALIAS_ELEMENT)) {
            //todo processAliasRegistration(ele);
        }
        else if (nodeNameEquals(ele, BEAN_ELEMENT)) {
            processBeanDefinition(ele);
        }
        else if (nodeNameEquals(ele, NESTED_BEANS_ELEMENT)) {
            // recurse
            doRegisterBeanDefinitions(ele);
        }
    }

    protected void processBeanDefinition(Element ele) {
        BeanDefinitionHolder bdHolder = parseBeanDefinitionElement(ele, null);
        beanDefinitionRegistry.registerBeanDefinition(bdHolder.getBeanName(), bdHolder.getBeanDefinition());
    }

    public BeanDefinitionHolder parseBeanDefinitionElement(Element ele, BeanDefinition containingBean) {
        String id = ele.getAttribute(ID_ATTRIBUTE);

        String beanName = id;

        if (containingBean == null) {
            checkNameUniqueness(beanName);
        }

        AbstractBeanDefinition beanDefinition = parseBeanDefinitionElement(ele, beanName, containingBean);
        if (beanDefinition != null) {
            if (!StringUtils.hasText(beanName)) {
                // 原本实现为判断是否有重复，有重复的名称依次 加#1，#2等
                beanName = beanDefinition.getBeanClassName();
            }
            return new BeanDefinitionHolder(beanDefinition, beanName);
        }

        return null;
    }

    protected void checkNameUniqueness(String beanName) {
        String foundName = null;

        if (StringUtils.hasText(beanName) && this.usedNames.contains(beanName)) {
            foundName = beanName;
        }
        if (foundName != null) {
            throw new BeansException("Bean name '" + foundName + "' is already used in this <beans> element");
        }

        this.usedNames.add(beanName);
    }

    public AbstractBeanDefinition parseBeanDefinitionElement(
            Element ele, String beanName, BeanDefinition containingBean) {

        String className = null;
        if (ele.hasAttribute(CLASS_ATTRIBUTE)) {
            className = ele.getAttribute(CLASS_ATTRIBUTE).trim();
        }

        try {
            String parent = null;
            if (ele.hasAttribute(PARENT_ATTRIBUTE)) {
                parent = ele.getAttribute(PARENT_ATTRIBUTE);
            }
            AbstractBeanDefinition bd = createBeanDefinition(className, parent, null);

            parseBeanDefinitionAttributes(ele, beanName, containingBean, bd);

            //todo 属性、构造器注入处理
//            parseConstructorArgElements(ele, bd);
//            parsePropertyElements(ele, bd);

            return bd;
        }
        catch (ClassNotFoundException ex) {
            throw new BeansException("Bean class [" + className + "] not found", ex);
        }
        catch (NoClassDefFoundError err) {
            throw new BeansException("Class that bean class [" + className + "] depends on not found", err);
        }
        catch (Throwable ex) {
            throw new BeansException("Unexpected failure during bean definition parsing", ex);
        }
    }

    private AbstractBeanDefinition createBeanDefinition(String className, String parentName, ClassLoader classLoader) throws ClassNotFoundException {
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setParentName(parentName);
        if (className != null) {
            if (classLoader != null) {
                bd.setBeanClass(ClassUtils.forName(className, classLoader));
            }
            else {
                bd.setBeanClassName(className);
            }
        }
        return bd;
    }

    private AbstractBeanDefinition parseBeanDefinitionAttributes(Element ele, String beanName,
                                                                BeanDefinition containingBean, AbstractBeanDefinition bd) {
        if (ele.hasAttribute(INIT_METHOD_ATTRIBUTE)) {
            String initMethodName = ele.getAttribute(INIT_METHOD_ATTRIBUTE);
            if (!"".equals(initMethodName)) {
                bd.setInitMethodName(initMethodName);
            }
        }

        if (ele.hasAttribute(DESTROY_METHOD_ATTRIBUTE)) {
            String destroyMethodName = ele.getAttribute(DESTROY_METHOD_ATTRIBUTE);
            if (!"".equals(destroyMethodName)) {
                bd.setDestroyMethodName(destroyMethodName);
            }
        }
        return bd;
    }

    private boolean isDefaultNamespace(Element element) {
        final String namespaceURI = element.getNamespaceURI();
        return isDefaultNamespace(namespaceURI);
    }

    private boolean isDefaultNamespace(String namespaceUri) {
        return BEANS_NAMESPACE_URI.equals(namespaceUri);
    }

    private boolean nodeNameEquals(Element node, String desiredName) {
        return desiredName.equals(node.getNodeName()) || desiredName.equals(getLocalName(node));
    }

    private String getLocalName(Node node) {
        return node.getLocalName();
    }
}
