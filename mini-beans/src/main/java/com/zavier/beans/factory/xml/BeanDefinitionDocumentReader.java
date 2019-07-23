package com.zavier.beans.factory.xml;

import com.zavier.beans.factory.support.BeanDefinitionRegistry;
import org.w3c.dom.Document;

public interface BeanDefinitionDocumentReader {

    void registerBeanDefinitions(Document doc, BeanDefinitionRegistry beanDefinitionRegistry);
}
