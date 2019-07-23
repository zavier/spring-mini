package com.zavier.beans.factory.support;

import com.zavier.beans.factory.config.BeanDefinition;
import com.zavier.beans.factory.xml.XmlBeanDefinitionReader;
import com.zavier.core.io.ClassPathResource;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultListableBeanFactoryTest {

    @Test
    public void testGetBean() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        ClassPathResource resource = new ClassPathResource("spring-beans.xml");
        beanDefinitionReader.loadBeanDefinitions(resource);

        final BeanDefinition simpleBean = beanFactory.getBeanDefinition("simpleBean");
        System.out.println(simpleBean);
    }
}