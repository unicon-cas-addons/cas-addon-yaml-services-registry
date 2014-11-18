package net.unicon.cas.addon.serviceregistry.yaml.config;

import net.unicon.cas.addon.serviceregistry.yaml.YamlServiceRegistry;
import org.jasig.cas.services.ServicesManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

/**
 * @author Dmitriy Kopylenko
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CasAddonYamlServiceRegistryNamespaceHandlerTests {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ServicesManager servicesManager;

    private static final String SERVICE_REGISTRY_DAO_BEAN_NAME = "serviceRegistryDao";

    @Test
    public void yamlServiceRegistryBeanDefinitionCorrectlyParsed() {
        assertTrue(applicationContext.containsBean(SERVICE_REGISTRY_DAO_BEAN_NAME));
        assertTrue(applicationContext.getBeansOfType(YamlServiceRegistry.class).size() == 1);
        assertTrue("3 services should have been loaded", servicesManager.getAllServices().size() == 3);
    }
}
