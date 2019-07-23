package com.zavier.beans.factory.xml;

import com.zavier.beans.BeansException;
import com.zavier.beans.factory.support.BeanDefinitionRegistry;
import com.zavier.core.io.Resource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;

public class XmlBeanDefinitionReader {
    private final BeanDefinitionRegistry registry;

    private DocumentLoader documentLoader = new DefaultDocumentLoader();

    private boolean namespaceAware = false;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public boolean isNamespaceAware() {
        return this.namespaceAware;
    }

    public int loadBeanDefinitions(Resource resource) {
        try (InputStream inputStream = resource.getInputStream()) {
            final InputSource inputSource = new InputSource(inputStream);
            return doLoadBeanDefinitions(inputSource, resource);
        } catch (IOException e) {
            throw new BeansException(e.getMessage(), e);
        }
    }

    private int doLoadBeanDefinitions(InputSource inputSource, Resource resource) {
        try {
            Document doc = doLoadDocument(inputSource, resource);
            return registerBeanDefinitions(doc, resource);
        } catch (Exception e) {
            throw new BeansException(e.getMessage());
        }
    }

    protected Document doLoadDocument(InputSource inputSource, Resource resource) throws Exception {
        return this.documentLoader.loadDocument(inputSource, null, null,
                1, isNamespaceAware());
    }

    public int registerBeanDefinitions(Document doc, Resource resource) {
        BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
        int countBefore = getRegistry().getBeanDefinitionCount();
        documentReader.registerBeanDefinitions(doc, registry);
        return getRegistry().getBeanDefinitionCount() - countBefore;
    }

    public final BeanDefinitionRegistry getRegistry() {
        return this.registry;
    }

    protected BeanDefinitionDocumentReader createBeanDefinitionDocumentReader() {
        return new DefaultBeanDefinitionDocumentReader();
    }
}
