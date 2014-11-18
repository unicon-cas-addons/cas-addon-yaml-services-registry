package net.unicon.cas.addon.serviceregistry.yaml.config;

import net.unicon.cas.addon.serviceregistry.externalconfig.RegisteredServicesExternalConfigSource;
import net.unicon.cas.addon.serviceregistry.yaml.YamlServiceRegistry;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * {@link org.springframework.beans.factory.xml.NamespaceHandler} for convenient <i>yaml services-registry</i> configuration namespace
 *
 * @author Dmitriy Kopylenko
 * @since 1.0.0
 */
public class CasAddonYamlServiceRegistryNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("yaml-services-registry", new JsonYamlServiceRegistryBeanDefinitionParser());
    }

    /**
     * Parses <pre>yaml-services-registry</pre> elements into bean definitions of type {@link net.unicon.cas.addon.serviceregistry.yaml
     * .YamlServiceRegistry}
     */
    private static class JsonYamlServiceRegistryBeanDefinitionParser extends AbstractBeanDefinitionParser {

        @Override
        protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
            final AbstractBeanDefinition jsonServiceRegistryBd = BeanDefinitionBuilder.genericBeanDefinition(YamlServiceRegistry.class)
                    .getBeanDefinition();

            final AbstractBeanDefinition externalConfigSourceBd =
                    BeanDefinitionBuilder.genericBeanDefinition(RegisteredServicesExternalConfigSource.class)
                            .addConstructorArgValue(element.getAttribute("config-file"))
                            .addConstructorArgReference("serviceRegistryDao")
                            .addConstructorArgReference("servicesManager").getBeanDefinition();

            parserContext.getRegistry().registerBeanDefinition(RegisteredServicesExternalConfigSource.class.getSimpleName(), externalConfigSourceBd);
            return jsonServiceRegistryBd;
        }

        @Override
        protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException {
            return "serviceRegistryDao";
        }
    }
}
