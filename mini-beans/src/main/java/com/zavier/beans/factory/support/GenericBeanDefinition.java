package com.zavier.beans.factory.support;

public class GenericBeanDefinition extends AbstractBeanDefinition {

    private String parentName;

    @Override
    public String getParentName() {
        return null;
    }

    @Override
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

}
