package xin.sunce.spring.namespace;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author lowrie
 * @date 2019-03-19
 */
public class ConfigNameSpaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("config", new ConfigBeanDefinitionParser());
    }
}
