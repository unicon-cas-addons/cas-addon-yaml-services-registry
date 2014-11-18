package net.unicon.cas.addon.serviceregistry.yaml;

import net.unicon.cas.addon.serviceregistry.externalconfig.AbstractExternalConfigLoadingServiceRegistry;
import net.xrrocha.yamltag.DefaultYamlFactory;
import org.jasig.cas.services.RegisteredService;

import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Specialization of {@code AbstractExternalConfigLoadingServiceRegistry} responsible for reading services definition from YAML configuration
 * file.
 *
 * Unmarshalling of the YAML payload is implemented with SnakeYAML and YAMLTag libraries.
 *
 * This implementation is thread safe.
 *
 * @author Dmitriy Kopylenko
 * @since 1.0.0
 */
public class YamlServiceRegistry extends AbstractExternalConfigLoadingServiceRegistry {

    private final Yaml yaml = new DefaultYamlFactory().newYaml();

    @Override
    @SuppressWarnings("unchecked")
    protected List<RegisteredService> doUnmarshal(Resource externalServicesDefinitionSource) throws IOException {
        try {
            final InputStream is = externalServicesDefinitionSource.getInputStream();
            final Map<String, List<RegisteredService>> records = this.yaml.loadAs(is, Map.class);
            if (records == null) {
                throw new IllegalStateException("YAML definition source is empty. Make sure to have a well-formed YAML file for registered services.");
            }
            final List<RegisteredService> services = records.get("services");
            if (services == null) {
                throw new IllegalStateException("YAML definition source is empty or missing top-level 'services' key. Make sure to have a " +
                        "well-formed YAML file for registered services.");
            }
            for (RegisteredService svc : services) {
                logger.info("Unmarshalled {}: {}", svc.getClass().getSimpleName(), svc);
            }
            return services;
        }
        catch (final FileNotFoundException e) {
            logger.warn("Resource [{}] does not exist.", externalServicesDefinitionSource);
        }
        catch (final YAMLException e) {
            throw new IllegalStateException("YAML definition source is malformed. Make sure to have a well-formed YAML file for registered services" +
                    ".", e);
        }
        return null;
    }
}
