package net.unicon.cas.addon.serviceregistry.yaml

import net.unicon.cas.addon.serviceregistry.externalconfig.ExternalConfigServiceRegistryException
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.FileSystemResource
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

/**
 *
 * @author Dmitriy Kopylenko
 */
class YamlServiceRegistryTests extends Specification {

    @Shared
    @Subject
    YamlServiceRegistry serviceRegistryUnderTest = new YamlServiceRegistry()

    def cleanup() {
        this.serviceRegistryUnderTest.registeredServices = []
    }

    def 'Regex, ANT and normal service definitions must all be loaded successfully from YAML file'() {
        given:
        def resource = new ClassPathResource("net/unicon/cas/addon/serviceregistry/yaml/servicesRegistry.yml")
        this.serviceRegistryUnderTest.unmarshalRegisteredServicesFrom(resource)

        expect:
        this.serviceRegistryUnderTest.load().size() == 3
    }

    def 'Loading a non-existing resource url should result in 0 services loaded'() {
        given:
        def resource = new FileSystemResource("http://localhost:8080/servicesRegistry.conf")
        this.serviceRegistryUnderTest.unmarshalRegisteredServicesFrom(resource)

        expect:
        this.serviceRegistryUnderTest.load().size() == 0
    }

    def 'Loading a non-existing resource file should result in 0 services loaded'() {
        given:
        def resource = new FileSystemResource("NonExistingYamlFile.yml")
        this.serviceRegistryUnderTest.unmarshalRegisteredServicesFrom(resource)

        expect:
        this.serviceRegistryUnderTest.load().size() == 0
    }

    def 'Loading an empty resource file should result in ExternalConfigServiceRegistryException'() {
        given:
        def resource = new ClassPathResource("net/unicon/cas/addon/serviceregistry/yaml/emptyServicesRegistry.yml")

        when:
        this.serviceRegistryUnderTest.unmarshalRegisteredServicesFrom(resource)

        then:
        thrown(ExternalConfigServiceRegistryException)
    }

    def 'Loading a malformed resource file should result in ExternalConfigServiceRegistryException'() {
        given:
        def resource = new ClassPathResource("net/unicon/cas/addon/serviceregistry/yaml/malformedServicesRegistry.yml")

        when:
        this.serviceRegistryUnderTest.unmarshalRegisteredServicesFrom(resource)

        then:
        thrown(ExternalConfigServiceRegistryException)
    }

    /*def 'Service definitions are loaded from a valid URL resource'() {
        given:
        def resource = new UrlResource("https://raw.githubusercontent" +
                ".com/unicon-cas-addons/cas-addon-json-yaml-services-registry/master/src/test/resources/net/unicon/cas/addon/serviceregistry" +
                "/yaml" +
                "/servicesRegistry.yml")
        this.serviceRegistryUnderTest.unmarshalRegisteredServicesFrom(resource)

        expect:
        this.serviceRegistryUnderTest.load().size() == 3
    }*/
}
